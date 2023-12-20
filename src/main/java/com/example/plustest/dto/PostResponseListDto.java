package com.example.plustest.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostResponseListDto extends CommonResponseDto{

    List<PostResponseDto> postResponseDto;

}
