package kr.ac.duksung.dukbab.Home;

import java.util.List;

public class OptionDTO {
    private String name;
    private List<String> optionContents; // 세부 옵션 목록

    public OptionDTO(String name, List<String> optionContents) {
        this.name = name;
        this.optionContents = optionContents;
    }

    public String getName() {
        return name;
    }

    public List<String> getOptionContents() {
        return optionContents;
    }
}
