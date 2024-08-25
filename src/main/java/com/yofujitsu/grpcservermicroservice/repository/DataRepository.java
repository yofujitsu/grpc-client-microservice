package com.yofujitsu.grpcservermicroservice.repository;

import com.yofujitsu.grpcservermicroservice.entity.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DataRepository extends JpaRepository<Data, Long> {

    /**
     * Retrieves a list of {@link Data} entities starting from the offset specified in the offsets table.
     * The number of entities returned is determined by the batchSize parameter.
     *
     * @param batchSize the maximum number of entities to retrieve
     * @return a list of {@link Data} entities
     */
    @Query(value = "select * from data offset (select current_offset from offsets) limit :batchSize", nativeQuery = true)
    List<Data> findAllWithOffset(@Param("batchSize") long batchSize);


    /**
     * Updates the current offset in the offsets table by incrementing it by the specified batch size.
     *
     * @param batchSize the amount by which to increment the current offset
     */
    @Modifying
    @Query(value = "update offsets set current_offset = current_offset + :batchSize", nativeQuery = true)
    void incrementOffset(@Param("batchSize") long batchSize);
}
