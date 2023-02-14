package com.example.smsdemo.controllers.student;

import com.example.smsdemo.HelloApplication;
import com.example.smsdemo.controllers.utils.DbUtil;
import com.example.smsdemo.controllers.utils.SceneChanger;
import com.example.smsdemo.models.*;
import com.example.smsdemo.models.courseSources.Attachment;
import com.example.smsdemo.models.courseSources.Discussion;
import com.example.smsdemo.models.courseSources.ResourceSection;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SubjectController implements Initializable {
    private static Course course;

    @FXML
    private FontAwesomeIconView courseLogo;
    @FXML
    private Label courseName;
    @FXML
    private Label courseID;
    @FXML
    private Label courseTeacher;
    @FXML
    private Label totalWeeks;
    @FXML
    private Label startDate;
    @FXML
    private Label endDate;
    @FXML
    private Label assignmentsCounter;
    @FXML
    private Label resourcesCounter;
    @FXML
    private TextArea discussionText;

    @FXML
    private VBox discussions;

    @FXML
    private VBox resources;

    private ArrayList<ResourceSection> resourceSections = new ArrayList<>();


    @FXML
    protected void onClassroomButton(ActionEvent event) throws Exception{
        SceneChanger.changeScene(event, "student/lms.fxml", "Classroom");
    }

    @FXML
    protected void onDashboardButton(ActionEvent event) throws Exception{
        SceneChanger.changeScene(event, "student/main-view.fxml", "Main Page");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            addAllDiscussions();
            addAllResources();
        }catch (Exception e){
            e.printStackTrace();
        }
        courseLogo.setGlyphName(course.getCourseLogo());
        courseName.setText(course.getCourseName());
        courseID.setText("C"+course.getCourseID());
        courseTeacher.setText("Course Teacher: "+
                course.getCourseTeacher().getName()+" " +
                course.getCourseTeacher().getSurname());
        startDate.setText("Start Date: "+course.getStartDate());
        endDate.setText("End Date: "+course.getEndDate());

    }


    public static void setCourse(Course course){
        SubjectController.course = course;
    }


//    private int getTotalWeeks(){
//        DateTime dateTime = new
//    }



    public void addAllDiscussions(){
        ArrayList<Discussion> discussionArrayList = new ArrayList<>();
        discussions.getChildren().removeAll(discussions.getChildren());
        DbUtil.setDatabase("sms_courses_discussions");

        try {

            ResultSet allDiscussions = DbUtil.dbExecuteQuery(String.format("SELECT * FROM c%s;", course.getCourseID()));

            while (allDiscussions.next()){

                Discussion newDiscussion = new Discussion();
                newDiscussion.setID(String.valueOf(allDiscussions.getInt("ID")));
                newDiscussion.setDate(allDiscussions.getString("date"));

                String senderID = allDiscussions.getString("sender");
                User sender;
                if (senderID.startsWith("T")) sender = new Teacher();
                else sender = new Student();
                sender.setUserID(senderID.substring(1));

                newDiscussion.setSender(sender);
                newDiscussion.setMessage(allDiscussions.getString("message"));

                discussionArrayList.add(newDiscussion);

            }

            allDiscussions.close();
            DbUtil.dbDisconnect();

            for (Discussion discussion: discussionArrayList){
                DbUtil.setDatabase("sms");
                User sender = discussion.getSender();

                System.out.println(sender.getUserType());
                System.out.println(sender.getUserID());

                ResultSet rest;

                if (sender.getUserType().equalsIgnoreCase("student"))
                    rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM student WHERE ID = %d;", Integer.valueOf(sender.getUserID())));
                else rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM teacher WHERE ID = %d;", Integer.valueOf(sender.getUserID())));

                rest.next();

                sender.setName(rest.getString("f_name"));
                sender.setSurname(rest.getString("s_name"));

                if (rest.getString("gender").equalsIgnoreCase("m")) sender.setGender("Male");
                else sender.setGender("Female");

                rest.close();
                DbUtil.dbDisconnect();


                HBox discussionBox = (HBox) FXMLLoader.load(HelloApplication.class.getResource("course/discussion.fxml"));
                for (Node child: discussionBox.getChildren()){
                    String childId = child.getId();
                    if (childId.equalsIgnoreCase("senderLogo")){
                        if (sender.getGender().equalsIgnoreCase("Male"))
                            ((ImageView) child).setImage(new Image(HelloApplication.class.getResourceAsStream("logo/avaM.png")));
                        else ((ImageView) child).setImage(new Image(HelloApplication.class.getResourceAsStream("logo/avaW.png")));
                    }else if (childId.equalsIgnoreCase("senderInfo")){
                        VBox senderInfoBox = (VBox) child;
                        ((Label) senderInfoBox.getChildren().get(0)).setText(sender.getName()+" "+sender.getSurname());
                        ((Label) senderInfoBox.getChildren().get(1)).setText(sender.getUserType());
                    } else if (childId.equalsIgnoreCase("messageInfo")) {
                        VBox messageInfoBox = (VBox) child;
                        ((Label) messageInfoBox.getChildren().get(0)).setText(discussion.getMessage());
                        ((Label) messageInfoBox.getChildren().get(1)).setText(discussion.getDate());
                    }
                }

                discussions.getChildren().add((Node) discussionBox);




            }


        }catch (Exception ex){
            ex.printStackTrace();
        }

    }


    @FXML
    protected void onSendBtn(){
        if (!discussionText.getText().isEmpty()){
            try {
                sendDiscussionMessage(discussionText.getText(), LogSession.getUser());
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }


    private void sendDiscussionMessage(String messageText, User user) throws Exception{
        DbUtil.setDatabase("sms_courses_discussions");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = formatter.format(LocalDate.now());
        DbUtil.dbExecuteUpdate(String.format("INSERT INTO c%s (date, sender, message) " +
                        "VALUES (\"%s\", \"%s\", \"%s\");", course.getCourseID(), date,
                String.valueOf(user.getUserType().toUpperCase().charAt(0))+user.getUserID(), messageText));
        addAllDiscussions();
        discussionText.setText("");
    }

    private void addAllResources() throws Exception{
        resourceSections.clear();
        DbUtil.setDatabase("sms_courses_resources");
        ResultSet rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM c%s;", course.getCourseID()));
        while (rest.next()){
            ResourceSection resourceSection = new ResourceSection();
            resourceSection.setID(String.valueOf(rest.getInt("ID")));
            resourceSection.setTopic(rest.getString("topic"));
            resourceSection.setDescription(rest.getString("description"));
            Teacher author = new Teacher();
            author.setUserID(rest.getString("author"));
            resourceSection.setAuthor(author);
            resourceSection.setDate(rest.getString("date"));
            resourceSections.add(resourceSection);
        }

        rest.close();
        DbUtil.dbDisconnect();

        for (ResourceSection section:resourceSections){
            DbUtil.setDatabase("sms");
            Teacher author = section.getAuthor();
            rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM teacher WHERE ID = %d;",
                    Integer.valueOf(author.getUserID())));
            if (rest.next()){
                author.setName(rest.getString("f_name"));
                author.setSurname(rest.getString("s_name"));
            }

            rest.close();
            DbUtil.dbDisconnect();
            addAllAttachments(section);
        }

        fillResourcesBox();


    }



    private void addAllAttachments(ResourceSection section) throws Exception{
        ArrayList<Attachment> attachments = new ArrayList<>();
        DbUtil.setDatabase("sms_courses_resources_attachments");
        ResultSet rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM c%s_r%s;", course.getCourseID(), section.getID()));
        while (rest.next()){
            Attachment attachment = new Attachment();
            attachment.setID(String.valueOf(rest.getInt("ID")));
            attachment.setResourceSectionID("r"+section.getID());
            attachment.setText(rest.getString("attachment_text"));

            try {
                File file = new File(String.format("course resources/%s/r%s/%s_%s",
                        course.getCourseID(),section.getID(), attachment.getID(),
                        rest.getString("attachment_file")));
                attachment.setFile(file);
                attachment.setSize(rest.getDouble("attachment_size"));
            }catch (Exception e){
                e.printStackTrace();
            }
            attachments.add(attachment);
        }

        rest.close();
        DbUtil.dbDisconnect();

        section.setAttachments(attachments);
    }


    private void fillResourcesBox() throws Exception{
        resources.getChildren().clear();
        for (ResourceSection section:resourceSections){
            HBox resourceSectionView = (HBox) FXMLLoader.load(HelloApplication.class.getResource("course/resource.fxml"));
            VBox sectionDetails = (VBox) resourceSectionView.getChildren().get(0);
            VBox attachmentDetails = (VBox) resourceSectionView.getChildren().get(1);

            ((Label) sectionDetails.getChildren().get(0)).setText(section.getTopic());
            ((Label) ((ScrollPane) sectionDetails.getChildren().get(1)).getContent()).setText(section.getDescription());
            ((Label) sectionDetails.getChildren().get(2)).setText(String.format("Uploaded by %s %s .",
                    section.getAuthor().getName(), section.getAuthor().getSurname()));
            ((Label) sectionDetails.getChildren().get(3)).setText(String.format("Date: %s", section.getDate()));

            for (Attachment attachment:section.getAttachments()){
                HBox attachmentView = (HBox) FXMLLoader.load(HelloApplication.class.getResource("course/attachment.fxml"));
                Label attachmentText = (Label) attachmentView.getChildren().get(0);
                Label attachmentLink = (Label) attachmentView.getChildren().get(1);
                Label attachmentSize = (Label) attachmentView.getChildren().get(2);

                attachmentText.setText(String.valueOf(section.getAttachments().indexOf(attachment)+1)+". "+attachment.getText());
                attachmentLink.setText(attachment.getFile().getName().substring(attachment.getFile().getName().indexOf('_')+1));
                attachmentLink.setOnMouseClicked(mouseEvent -> {
                    try {
                        File newFile = new File(String.format("user files/%s",
                                attachment.getFile().getName().substring(attachment.getFile().getName().indexOf('_')+1)));
                        if (newFile.exists()) newFile.delete();
                        newFile.createNewFile();
                        FileChannel src = new FileInputStream(attachment.getFile().getAbsolutePath()).getChannel();
                        FileChannel dest = new FileOutputStream(newFile.getAbsolutePath()).getChannel();
                        try {
                            dest.transferFrom(src, 0, src.size());
                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            src.close();
                            dest.close();
                        }

                        Desktop.getDesktop().open(newFile);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
                attachmentSize.setText(String.format("%.3f kb", attachment.getSize()));

                ((VBox) ((ScrollPane) attachmentDetails.getChildren().get(1)).getContent()).getChildren().add((Node) attachmentView);
            }

            resources.getChildren().add((Node) resourceSectionView);

        }

    }





}
