package Afriends_v3.entity;

import lombok.Data;
import java.util.List;

@Data
public class EntityList<T> {
    private List<T> records;
    private long total;
    private long current;
    private long size;

    public static <T> EntityList<T> of(List<T> records, long total, long current, long size) {
        EntityList<T> entityList = new EntityList<>();
        entityList.setRecords(records);
        entityList.setTotal(total);
        entityList.setCurrent(current);
        entityList.setSize(size);
        return entityList;
    }
}
