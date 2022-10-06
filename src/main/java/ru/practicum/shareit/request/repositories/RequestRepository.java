package ru.practicum.shareit.request.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.ItemRequest;

import java.util.Collection;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {
    @Modifying
    @Query("select ir from ItemRequest as ir " +
            "where ir.userId = ?1 " +
            "order by ir.creationTime desc ")
    Collection<ItemRequest> findAllByUserId(Long userId);

    Page<ItemRequest> findAllByUserIdNot(Long userId, Pageable pageRequest);
}
