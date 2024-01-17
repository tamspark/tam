package com.sparklab.TAM.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "message_history")
public class MessageHistory  extends  BaseEntity{


    @Lob
    @Column(length = Integer.MAX_VALUE)
    private String requestMessage;
    @Lob
    @Column(length = Integer.MAX_VALUE)
    private String responseMessage;
    private long timestamp;

    @ManyToOne
    @JoinColumn(name = "thread_id", referencedColumnName = "id",nullable = false)
    @JsonIgnore
    private ThreadHistory threadHistory;


}
