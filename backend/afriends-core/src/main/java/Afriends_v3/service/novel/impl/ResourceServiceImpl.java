package Afriends_v3.service.novel.impl;

import Afriends_v3.core.common.constant.ErrorCodeEnum;
import Afriends_v3.core.common.exception.BusinessException;
import Afriends_v3.core.common.resp.RestResp;
import Afriends_v3.core.constant.SystemConfigConsts;
import Afriends_v3.dto.resp.ImgVerifyCodeRespDto;
import Afriends_v3.manager.redis.VerifyCodeManager;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import Afriends_v3.service.novel.ResourceService;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 资源（图片/视频/文档）相关服务实现类
 *
 * @author xiongxiaoyang
 * @date 2022/5/17
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceServiceImpl implements ResourceService {

    private final VerifyCodeManager verifyCodeManager;

    @Value("${novel.file.upload.path}")
    private String fileUploadPath;

    @Override
    public RestResp<ImgVerifyCodeRespDto> getImgVerifyCode() throws IOException {
        String sessionId = IdWorker.get32UUID();
        return RestResp.ok(ImgVerifyCodeRespDto.builder()
            .sessionId(sessionId)
            .img(verifyCodeManager.genImgVerifyCode(sessionId))
            .build());
    }

    @SneakyThrows
    @Override
    public RestResp<String> uploadImage(MultipartFile file) {
        LocalDateTime now = LocalDateTime.now();
        String savePath =
            SystemConfigConsts.IMAGE_UPLOAD_DIRECTORY
                + now.format(DateTimeFormatter.ofPattern("yyyy")) + File.separator
                + now.format(DateTimeFormatter.ofPattern("MM")) + File.separator
                + now.format(DateTimeFormatter.ofPattern("dd"));
        String oriName = file.getOriginalFilename();
        // 替换 assert oriName != null;
        if (oriName == null || oriName.trim().isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_ERROR);
        }
        String saveFileName = IdWorker.get32UUID() + oriName.substring(oriName.lastIndexOf("."));
        // 替换原 File saveFile = ... 这行
        File saveDir = new File(fileUploadPath, savePath);  // 先拼接根路径+日期路径
        if (!saveDir.exists() && !saveDir.mkdirs()) {
            throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_ERROR);
        }
        File saveFile = new File(saveDir, saveFileName);     // 再拼接目录+文件名
//        if (!saveFile.getParentFile().exists()) {
//            boolean isSuccess = saveFile.getParentFile().mkdirs();
//            if (!isSuccess) {
//                throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_ERROR);
//            }
//        }
        // 打印路径，验证是否正确（如 C:/Users/xiongxiaoyang/upload/xxx/2025/10/14/xxx.jpg）
        System.out.println("最终保存路径：" + saveFile.getAbsolutePath());
        file.transferTo(saveFile);
        // 替换原图片校验逻辑
        try (InputStream is = new FileInputStream(saveFile)) {
            if (Objects.isNull(ImageIO.read(is))) {
                Files.deleteIfExists(saveFile.toPath());
                throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_TYPE_NOT_MATCH);
            }
        } catch (IOException e) {
            Files.deleteIfExists(saveFile.toPath());  // 读取失败也删除文件
            throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_ERROR);
        }
        return RestResp.ok(savePath + File.separator + saveFileName);
    }

}
