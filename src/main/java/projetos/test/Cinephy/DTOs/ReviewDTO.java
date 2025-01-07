package projetos.test.Cinephy.DTOs;

public class ReviewDTO {
   private String user;
   private double rating;
   private String comment;


    public ReviewDTO(String comment, double rating, String user) {
        this.user = user;
        this.rating = rating;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
