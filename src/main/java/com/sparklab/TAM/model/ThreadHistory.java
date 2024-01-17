package com.sparklab.TAM.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "thread_history")
public class ThreadHistory extends BaseEntity{

    public ThreadHistory(String threadId) {
        this.threadId = threadId;
    }

    private String threadId;

    private Long createdTime = System.currentTimeMillis();

    @OneToMany(mappedBy = "threadHistory", cascade = CascadeType.ALL)
    private List<MessageHistory> messages;
    
}
