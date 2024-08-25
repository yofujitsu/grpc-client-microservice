package com.yofujitsu.grpcservermicroservice.service.data;

import com.yofujitsu.grpcservermicroservice.entity.Data;
import com.yofujitsu.grpcservermicroservice.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataServiceImpl implements DataService{

    private final DataRepository dataRepository;

    @Override
    public void handle(Data data) {
        dataRepository.save(data);
        log.info("Data object {} saved", data.toString());
    }

    /**
     * Retrieves a batch of {@link Data} entities from the database, starting from the current offset specified in the offsets table.
     * The number of entities returned is determined by the batchSize parameter.
     * After retrieving the data, the current offset is incremented by the size of the returned batch,
     * provided that the batch is not empty.
     *
     * @param batchSize the maximum number of entities to retrieve
     * @return a list of {@link Data} entities
     */
    @Override
    public List<Data> getWithBatch(long batchSize) {
        // Retrieve batch of data from the database
        List<Data> data = dataRepository.findAllWithOffset(batchSize);

        // If the batch is not empty, increment the current offset
        if(!data.isEmpty()) {
            // Calculate the size of the batch to be incremented
            long batchSizeToIncrement = Long.min(batchSize, data.size());
            dataRepository.incrementOffset(batchSizeToIncrement);
        }

        return data;
    }
}
