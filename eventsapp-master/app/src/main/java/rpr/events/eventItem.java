package rpr.events;



// Classe qui matérialise ce qu'est un événement ( Modèle )
public class eventItem {

    // ATTRIBUTS
    private int event_id; // id d'event
    private String name; // nom d'event
    private String time; // date début de l'event
    private String venue; // localisation
    private String details; // infos sur l'event

    private int usertype_id; // id du type d'utilisateur (1 : Provider | 2 : User)
    private String usertype;

    private String categoryPlace;


    private int creator_id;
    private String creator;
    private int category_id;
    private String category;

    private String imageEvt;

    private String time_end; // date début de l'event

    private int price;
    private int attendance; // Nombre de places dispo


    // CONSTRUCTEUR
    public eventItem(int event_id, String name, String time, String venue, String details, String usertype, int creator_id, String creator, int category_id, String category,String image,String time_end,int price,int attendance) {
        this.event_id = event_id;
        this.name = name;
        this.time = time;
        this.venue = venue;
        this.details = details;
        this.creator_id = creator_id;
        this.creator = creator;
        this.category_id = category_id;
        this.category = category;
        this.imageEvt = image;
        this.time_end = time_end;
        this.price = price;
        this.attendance = attendance;
    }

            // GETTERS ET SETTERS

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getUsertype_id() {
        return usertype_id;
    }

    public void setUsertype_id(int usertype_id) {
        this.usertype_id = usertype_id;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getCategoryPlace() {
        return categoryPlace;
    }

    public void setCategoryPlace(String categoryPlace) {
        this.categoryPlace = categoryPlace;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return imageEvt;
    }

    public void setImage(String image) {
        this.imageEvt = image;
    }

    public String getTime_end() { return time_end; }

    public void setTime_end(String time_end) { this.time_end = time_end; }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAttendance() { return attendance; }

    public void setAttendance(int attendance) { this.attendance = attendance; }
}
