package projetos.test.Cinephy.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OmdbSearchResponse {

    @JsonProperty("Search")
    private List<OmdbResponse> search;
    @JsonProperty("Response")
    private String response;
    private String totalResults;

    @JsonProperty("Error")
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<OmdbResponse> getSearch() {
        return search;
    }

    public void setSearch(List<OmdbResponse> search) {
        this.search = search;
    }
}
