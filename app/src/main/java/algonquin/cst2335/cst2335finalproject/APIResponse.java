package algonquin.cst2335.finalprojectassignment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class APIResponse {
    @SerializedName("definitions")
    @Expose
    private List<OwlBotDefinition> definitions = null;
    @SerializedName("word")
    @Expose
    private String word;
    @SerializedName("pronunciation")
    @Expose
    private String pronunciation;

    public List<OwlBotDefinition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<OwlBotDefinition> definitions) {
        this.definitions = definitions;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }
}

