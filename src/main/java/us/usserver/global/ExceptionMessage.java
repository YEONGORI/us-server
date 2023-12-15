package us.usserver.global;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ExceptionMessage {

    public static final String Novel_NOT_FOUND = "해당 소설이 존재 하지 않습니다.";
    public static final String Chapter_NOT_FOUND = "해당 화가 존재 하지 않습니다.";
    public static final String Paragraph_NOT_FOUND = "해당 한줄이 존재 하지 않습니다.";
    public static final String Author_NOT_FOUND = "해당 작가가 존재 하지 않습니다.";
    public static final String Main_Author_NOT_MATCHED = "해당 작가와 메인 작가가 일치 하지 않습니다.";
    public static final String Exceed_Paragraph_Length = "해당 한줄의 길이가 너무 깁니다. (300자 이하)";
    public static final String Exceed_Score_Range = "1 ~ 10 사이의 평점을 입력해 주세요.";
}
