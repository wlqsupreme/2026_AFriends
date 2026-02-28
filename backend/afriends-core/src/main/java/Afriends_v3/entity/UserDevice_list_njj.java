package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户设备信息列表
 * 对应实体: UserDevice_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserDevice_list_njj extends EntityList<UserDevice_njj> {

    // 内存存储
    private static final Map<Long, UserDevice_njj> userDeviceCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserDevice_njj>> userDeviceListCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserDevice = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserDevice_njj> allUserDevice) {
        try {
            System.out.println("开始直接加载用户设备数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserDevice == null || allUserDevice.isEmpty()) {
                System.out.println("用户设备数据为空，清空缓存并设置状态");
                totalUserDevice = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户设备数据直接加载完成！总数: " + totalUserDevice);
                return;
            }

            // 存储到内存缓存
            for (UserDevice_njj device : allUserDevice) {
                if (device != null && device.getId() != null) {
                    System.out.println("处理用户设备数据: ID=" + device.getId() + ", 用户ID=" + device.getUserId());
                    userDeviceCache.put(device.getId(), device);

                    // 按用户ID分组存储
                    userDeviceListCache.computeIfAbsent(device.getUserId(), k -> new ArrayList<>()).add(device);
                }
            }

            // 更新统计信息
            totalUserDevice = userDeviceCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户设备数据直接加载完成！总数: " + totalUserDevice);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户设备数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserDevice = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户设备数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserDevice_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户设备数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户设备数据
            System.out.println("正在执行数据库查询...");
            List<UserDevice_njj> allUserDevice = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserDevice.size() + " 条记录");

            // 存储到内存缓存
            for (UserDevice_njj device : allUserDevice) {
                if (device != null && device.getId() != null) {
                    System.out.println("处理用户设备数据: ID=" + device.getId() + ", 用户ID=" + device.getUserId());
                    userDeviceCache.put(device.getId(), device);

                    // 按用户ID分组存储
                    userDeviceListCache.computeIfAbsent(device.getUserId(), k -> new ArrayList<>()).add(device);
                }
            }

            // 更新统计信息
            totalUserDevice = userDeviceCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户设备数据加载完成！总数: " + totalUserDevice);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户设备数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户设备数据（从内存）
     */
    public static List<UserDevice_njj> getAllUserDevice() {
        return new ArrayList<>(userDeviceCache.values());
    }

    /**
     * 根据ID获取用户设备数据
     */
    public static UserDevice_njj getUserDeviceById(Long id) {
        return userDeviceCache.get(id);
    }

    /**
     * 根据用户ID获取用户设备数据
     */
    public static List<UserDevice_njj> getUserDeviceByUserId(Long userId) {
        return userDeviceListCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据设备类型获取用户设备数据
     */
    public static List<UserDevice_njj> getUserDeviceByDeviceType(String deviceType) {
        List<UserDevice_njj> result = new ArrayList<>();
        for (UserDevice_njj device : userDeviceCache.values()) {
            if (device.getDeviceType() != null && device.getDeviceType().equals(deviceType)) {
                result.add(device);
            }
        }
        return result;
    }

    /**
     * 根据当前设备状态获取用户设备数据
     */
    public static List<UserDevice_njj> getUserDeviceByIsCurrent(Byte isCurrent) {
        List<UserDevice_njj> result = new ArrayList<>();
        for (UserDevice_njj device : userDeviceCache.values()) {
            if (device.getIsCurrent() != null && device.getIsCurrent().equals(isCurrent)) {
                result.add(device);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUserDevice", totalUserDevice);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userDeviceCache.clear();
        userDeviceListCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserDevice_njj> mapper) {
        loadFromDatabase(mapper);
    }

    /**
     * 添加设备到缓存
     */
    public static void addDevice(UserDevice_njj device) {
        if (device != null && device.getId() != null) {
            userDeviceCache.put(device.getId(), device);
            userDeviceListCache.computeIfAbsent(device.getUserId(), k -> new ArrayList<>()).add(device);
            totalUserDevice = userDeviceCache.size();
            System.out.println("设备已添加到缓存: ID=" + device.getId() + ", 用户ID=" + device.getUserId());
        }
    }

    /**
     * 更新设备信息
     */
    public static void updateDevice(UserDevice_njj device) {
        if (device != null && device.getId() != null) {
            userDeviceCache.put(device.getId(), device);
            // 更新用户设备列表缓存
            List<UserDevice_njj> userDevices = userDeviceListCache.get(device.getUserId());
            if (userDevices != null) {
                for (int i = 0; i < userDevices.size(); i++) {
                    if (userDevices.get(i).getId().equals(device.getId())) {
                        userDevices.set(i, device);
                        break;
                    }
                }
            } else {
                // 如果用户设备列表不存在，创建新的列表
                userDeviceListCache.put(device.getUserId(), new ArrayList<>(List.of(device)));
            }
            System.out.println("设备信息已更新: ID=" + device.getId() + ", 用户ID=" + device.getUserId());
        }
    }

    /**
     * 从缓存中移除设备
     */
    public static void removeDevice(Long deviceId) {
        UserDevice_njj device = userDeviceCache.remove(deviceId);
        if (device != null && device.getUserId() != null) {
            List<UserDevice_njj> userDevices = userDeviceListCache.get(device.getUserId());
            if (userDevices != null) {
                userDevices.removeIf(d -> d.getId().equals(deviceId));
                // 如果用户设备列表为空，可以移除该用户的缓存
                if (userDevices.isEmpty()) {
                    userDeviceListCache.remove(device.getUserId());
                }
            }
            totalUserDevice = userDeviceCache.size();
            System.out.println("设备已从缓存移除: ID=" + deviceId + ", 用户ID=" + device.getUserId());
        }
    }

    /**
     * 批量移除设备
     */
    public static void removeDevices(List<Long> deviceIds) {
        for (Long deviceId : deviceIds) {
            removeDevice(deviceId);
        }
        System.out.println("批量移除设备完成，共移除 " + deviceIds.size() + " 台设备");
    }
}
