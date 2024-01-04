package us.usserver.global;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ExceptionMessage {
    public static final String Novel_NOT_FOUND = "해당 소설이 존재 하지 않습니다.";
    public static final String Chapter_NOT_FOUND = "해당 화가 존재 하지 않습니다.";
    public static final String Paragraph_NOT_FOUND = "해당 한줄이 존재 하지 않습니다.";
    public static final String Author_NOT_FOUND = "해당 작가가 존재 하지 않습니다.";
    public static final String Comment_NOT_FOUND = "해당 댓글이 존재 하지 않습니다.";
    public static final String Valid_ModelAttribute_NOT_FOUND = "ModelAttribute 값이 유효하지 않습니다.";
    public static final String Valid_RequestBody_NOT_FOUND = "RequestBody 값이 유효하지 않습니다.";
    public static final String Main_Author_NOT_MATCHED = "해당 작가와 메인 작가가 일치 하지 않습니다.";
    public static final String Author_NOT_AUTHORIZED = "허가 되지 않은 작가입니다.";
    public static final String Paragraph_Length_OUT_OF_RANGE = "해당 한줄의 길이가 범위를 벗어났습니다. (50 ~ 300자)";
    public static final String Comment_Length_OUT_OF_RANGE = "해당 댓글의 길이가 범위를 벗어났습니다. (1 ~ 300자)";
    public static final String Score_OUT_OF_RANGE = "1 ~ 10 사이의 평점을 입력해 주세요.";
}
