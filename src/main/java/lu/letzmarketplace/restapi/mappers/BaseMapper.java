package lu.letzmarketplace.restapi.mappers;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseMapper<D, E> {
    public E toEntity(D dto) {
        return null;
    }

    public D toDto(E entity) {
        return null;
    }

    public List<E> toEntityList(List<D> dtoList) {
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    List<D> toDtoList(List<E> entityList) {
        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
