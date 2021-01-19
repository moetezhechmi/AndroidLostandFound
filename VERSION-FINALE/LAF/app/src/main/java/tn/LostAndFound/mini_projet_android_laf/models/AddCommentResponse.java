package tn.LostAndFound.mini_projet_android_laf.models;

public class AddCommentResponse
{
    String message;
    int status;
    String commentId,date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    @Override
    public String toString() {
        return "AddCommentResponse{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", commentId='" + commentId + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
