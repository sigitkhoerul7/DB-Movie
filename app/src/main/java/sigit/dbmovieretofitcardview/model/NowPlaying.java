
package sigit.dbmovieretofitcardview.model;

import java.util.List;
//import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

//@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class NowPlaying {

    @SerializedName("dates")
    private Dates mDates;
    @SerializedName("page")
    private Integer mPage;
    @SerializedName("results")
    private List<Result> mResults;
    @SerializedName("total_pages")
    private Integer mTotalPages;
    @SerializedName("total_results")
    private Integer mTotalResults;

    public Dates getDates() {
        return mDates;
    }

    public void setDates(Dates dates) {
        mDates = dates;
    }

    public Integer getPage() {
        return mPage;
    }

    public void setPage(Integer page) {
        mPage = page;
    }

    public List<Result> getResults() {
        return mResults;
    }

    public void setResults(List<Result> results) {
        mResults = results;
    }

    public Integer getTotalPages() {
        return mTotalPages;
    }

    public void setTotalPages(Integer totalPages) {
        mTotalPages = totalPages;
    }

    public Integer getTotalResults() {
        return mTotalResults;
    }

    public void setTotalResults(Integer totalResults) {
        mTotalResults = totalResults;
    }

}
