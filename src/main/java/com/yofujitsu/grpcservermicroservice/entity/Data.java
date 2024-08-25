package com.yofujitsu.grpcservermicroservice.entity;

import com.yofujitsu.grpccommon.GRPCData;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "data")
@NoArgsConstructor
@Getter
@Setter
public class Data {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long heroId;

    private LocalDateTime timestamp;

    private double value;

    @Column(name = "value_type")
    @Enumerated(EnumType.STRING)
    private ValueType valueType;

    public Data(GRPCData grpcData) {
        this.id = grpcData.getId();
        this.heroId = grpcData.getHeroId();
        this.timestamp = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(
                        grpcData.getTimestamp().getSeconds(),
                        grpcData.getTimestamp().getNanos()
                ),
                ZoneId.of("UTC")
        );
        this.value = grpcData.getValue();
        this.valueType = ValueType.valueOf(
                grpcData.getValueType().name()
        );
    }
}
