package com.sparklab.TAM.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "openai_assistant")
public class OpenAIAssistant extends BaseEntity{

    private String assistantId;
    private List<String> fileIds;

    @Column(length = 65535)
    private String instructions;
    private String name;
    private Long createdTime = System.currentTimeMillis();

}
