package com.code_words.api.domain.beans;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Player {
    String username;
    Long score;
}
