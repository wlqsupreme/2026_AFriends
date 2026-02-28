package Afriends_v3.service;

import Afriends_v3.entity.ProductsBase_njj;
import Afriends_v3.entity.ProductsBase_list_njj;
import Afriends_v3.mapper.ProductsBaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 产品基础信息服务类
 */
@Service
public class ProductsBaseService {

    @Autowired
    private ProductsBaseMapper productsBaseMapper;

    /**
     * 加载产品基础信息数据到内存
     */
    public void loadProductsBaseToMemory() {
        try {
            System.out.println("ProductsBaseService: 开始加载产品基础信息数据到内存...");
            ProductsBase_list_njj.loadFromDatabase(productsBaseMapper);
            System.out.println("ProductsBaseService: 产品基础信息数据加载完成");
        } catch (Exception e) {
            System.err.println("ProductsBaseService: 加载产品基础信息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有产品基础信息（从内存）
     */
    public List<ProductsBase_njj> getAllProductsBaseFromMemory() {
        return ProductsBase_list_njj.getAllProductsBase();
    }

    /**
     * 根据状态获取产品基础信息（从内存）
     */
    public List<ProductsBase_njj> getProductsBaseByStatusFromMemory(Byte status) {
        return ProductsBase_list_njj.getProductsBaseByStatus(status);
    }

    /**
     * 获取可用的产品基础信息（状态为1的产品）
     */
    public List<ProductsBase_njj> getAvailableProductsFromMemory() {
        return ProductsBase_list_njj.getProductsBaseByStatus((byte) 1);
    }

    /**
     * 根据产品ID获取产品基础信息（从内存）
     */
    public ProductsBase_njj getProductsBaseByIdFromMemory(Long coinRechargeId) {
        return ProductsBase_list_njj.getProductsBaseById(coinRechargeId);
    }

    /**
     * 获取产品基础信息统计信息
     */
    public Map<String, Object> getProductsBaseStatisticsFromMemory() {
        return ProductsBase_list_njj.getStatistics();
    }

    /**
     * 强制刷新产品基础信息数据
     */
    public void refreshProductsBaseData() {
        loadProductsBaseToMemory();
    }
}
