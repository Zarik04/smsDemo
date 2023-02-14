package com.example.smsdemo.controllers.teacher;

import com.example.smsdemo.HelloApplication;
import com.example.smsdemo.controllers.utils.DbUtil;
import com.example.smsdemo.controllers.utils.SceneChanger;
import com.example.smsdemo.models.*;
import com.example.smsdemo.models.courseSources.Attachment;
import com.example.smsdemo.models.courseSources.Discussion;
import com.example.smsdemo.models.courseSources.ResourceSection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.IDN;
import java.net.URI;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.awt.Desktop;


public class SubjectViewController implements Initializable {

    private static Course subject;

    @FXML
    private ChoiceBox<String> groupList;

    @FXML
    private TableView<Student> enrolledStudents;

    @FXML
    private TableColumn<Student, Integer> counter;

    @FXML
    private TableColumn<Student, String> studentID;

    @FXML
    private TableColumn<Student, String> studentGroup;

    @FXML
    private TableColumn<Student, String> studentName;

    @FXML
    private TableColumn<Student, String> studentSurname;

    @FXML
    private TableColumn<Student, String> studentMiddleName;

    @FXML
    private TableColumn<Student, String> studentGender;

    @FXML
    private TableColumn<Student, String> studentBirthDate;

    @FXML
    private TableColumn<Student, String> studentPhone;

    @FXML
    private TableColumn<Student, String> studentEmail;

    @FXML
    private VBox discussions;
    @FXML
    private TextArea discussionText;

    @FXML
    private VBox resources;

    @FXML
    private TextField sectionTopic;
    @FXML
    private TextArea sectionDescription;
    private ArrayList<File> sectionFiles = new ArrayList<>();

    @FXML
    private VBox assignments;
    @FXML
    private TextField assignmentTopic;
    @FXML
    private TextArea assignmentDescription;
    @FXML
    private ChoiceBox<String> groupList1;
    @FXML
    private DatePicker assignmentStartDate;
    @FXML
    private DatePicker assignmentEndDate;
    @FXML
    private TextField assignmentMaxScore;
    @FXML
    private TextField assignmentMinScore;


    private ObservableList<Student> studentObservableList = FXCollections.observableArrayList();
    private ObservableList<String> groupIdList = FXCollections.observableArrayList();

    private ArrayList<ResourceSection> resourceSections = new ArrayList<>();





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            populateStudentsTable("SELECT ALL");
            setGroupList();
            addAllDiscussions();
            addAllResources();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @FXML
    protected void onDashboardButton(ActionEvent event) throws Exception{
        SceneChanger.changeScene(event, "teacher/main-teacher.fxml", "Main Page");
    }

    @FXML
    protected void onSubjectsButton(ActionEvent event) throws Exception{
        SceneChanger.changeScene(event, "teacher/teacher-subjects.fxml", "Subjects");
    }

    @FXML
    protected void onChooseGroupButton(ActionEvent event) throws Exception{
        String selectedID = groupList.getSelectionModel().getSelectedItem();
        if (selectedID!=null){
            populateStudentsTable(selectedID);
        }
    }


    private void populateStudentsTable(String groupID) throws Exception{
        ArrayList<Integer> studentsID = new ArrayList<>();
        ArrayList<Student> students = new ArrayList<>();

        enrolledStudents.getItems().clear();

        DbUtil.setDatabase("sms");
        ResultSet rest = DbUtil.dbExecuteQuery("SELECT * FROM student;");

        while (rest.next()){
            studentsID.add(rest.getInt("ID"));
        }
        rest.close();
        DbUtil.dbDisconnect();

        DbUtil.setDatabase("sms_courses");
        for (int ID:studentsID){
            Student student = new Student();
            student.setUserID(String.valueOf(ID));

            rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM s%s WHERE " +
                            "course_id = \"C%s\" AND course_name = \"%s\";", student.getUserID(),
                    subject.getCourseID(), subject.getCourseName()));

            if (rest.next()) students.add(student);
            rest.close();
            DbUtil.dbDisconnect();
        }

        DbUtil.setDatabase("sms");
        for (Student student:students){
            rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM student " +
                    "WHERE ID = %d;", Integer.valueOf(student.getUserID())));
            if (!rest.next()) continue;

            student.setGroupID(rest.getString("group_id"));
            student.setName(rest.getString("f_name"));
            student.setSurname(rest.getString("s_name"));
            student.setMiddleName(rest.getString("m_name"));
            if (rest.getString("gender").equalsIgnoreCase("m")) student.setGender("Male");
            else student.setGender("Female");
            student.setBirthDate(rest.getString("birth_date"));
            student.setPhone("+"+rest.getString("phone"));
            student.setUserName(rest.getString("email"));
            if (rest.getInt("log_status")==1) student.setOnline(true);
            else student.setOnline(false);
            student.setLogStatus();
            if (rest.getInt("reg_status")==1) student.setRegistered(true);
            else student.setRegistered(false);
            student.setRegStatus();


        }



        if (groupID.equalsIgnoreCase("select all")){
            int cnt = 0;

            for (Student student:students){
                cnt++;
                student.setCounter(cnt);
                studentObservableList.add(student);
            }

        }else {
            int cnt = 0;
            for (Student student:students){
                if (student.getGroupID().equals(groupID)){
                    cnt++;
                    student.setCounter(cnt);
                    studentObservableList.add(student);
                }
            }
        }


        counter.setCellValueFactory(new PropertyValueFactory<Student, Integer>("counter"));
        studentID.setCellValueFactory(new PropertyValueFactory<Student, String>("userID"));
        studentGroup.setCellValueFactory(new PropertyValueFactory<Student, String>("groupID"));
        studentName.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        studentSurname.setCellValueFactory(new PropertyValueFactory<Student, String>("surname"));
        studentMiddleName.setCellValueFactory(new PropertyValueFactory<Student, String>("middleName"));
        studentGender.setCellValueFactory(new PropertyValueFactory<Student, String>("gender"));
        studentBirthDate.setCellValueFactory(new PropertyValueFactory<Student, String>("birthDate"));
        studentPhone.setCellValueFactory(new PropertyValueFactory<Student, String>("phone"));
        studentEmail.setCellValueFactory(new PropertyValueFactory<Student, String>("userName"));


        enrolledStudents.setItems(studentObservableList);


    }

    private void setGroupList() throws Exception{
        groupIdList.add("SELECT ALL");
        for (Student student:studentObservableList){
            groupIdList.add(student.getGroupID());
        }
        groupList.setItems(groupIdList);
    }

    public static Course getSubject() {
        return subject;
    }

    public static void setSubject(Course subject) {
        SubjectViewController.subject = subject;
    }



    public void addAllDiscussions(){
        ArrayList<Discussion> discussionArrayList = new ArrayList<>();
        discussions.getChildren().removeAll(discussions.getChildren());
        DbUtil.setDatabase("sms_courses_discussions");

        try {

            ResultSet allDiscussions = DbUtil.dbExecuteQuery(String.format("SELECT * FROM c%s;", subject.getCourseID()));

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
                        "VALUES (\"%s\", \"%s\", \"%s\");", subject.getCourseID(), date,
                String.valueOf(user.getUserType().toUpperCase().charAt(0))+user.getUserID(), messageText));
        addAllDiscussions();
        discussionText.setText("");
    }


    private void addAllResources() throws Exception{
        resourceSections.clear();
        DbUtil.setDatabase("sms_courses_resources");
        ResultSet rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM c%s;", subject.getCourseID()));
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
        ResultSet rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM c%s_r%s;", subject.getCourseID(), section.getID()));
        while (rest.next()){
            Attachment attachment = new Attachment();
            attachment.setID(String.valueOf(rest.getInt("ID")));
            attachment.setResourceSectionID("r"+section.getID());
            attachment.setText(rest.getString("attachment_text"));

            try {
                File file = new File(String.format("course resources/%s/r%s/%s_%s",
                        subject.getCourseID(),section.getID(), attachment.getID(),
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

    @FXML
    protected void onChooseFileBtn(ActionEvent event){
        sectionFiles.clear();
        FileChooser newChooser = new FileChooser();
        newChooser.setInitialDirectory(new File(String.format("C:/Users/%s", System.getProperty("user.name"))));
        sectionFiles.addAll(newChooser.showOpenMultipleDialog(new Stage()));
    }


    @FXML
    protected void onUploadSectionBtn(ActionEvent event) throws Exception{
        sectionTopic.setText(sectionTopic.getText().trim());
        sectionDescription.setText(sectionDescription.getText().trim());
        if (!sectionTopic.getText().isEmpty()&&!sectionDescription.getText().isEmpty()&&!sectionFiles.isEmpty()){
            ResourceSection newSection = new ResourceSection();
            ArrayList<Attachment> attachments = new ArrayList<>();
            ResultSet rest;
            newSection.setTopic(sectionTopic.getText());
            newSection.setDescription(sectionDescription.getText());
            newSection.setAuthor((Teacher) LogSession.getUser());
            newSection.setDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            DbUtil.setDatabase("sms_courses_resources");
            DbUtil.dbExecuteUpdate(String.format("INSERT INTO c%s (topic, description, author, date) " +
                    "VALUES (\"%s\",\"%s\",\"%s\",\"%s\");", subject.getCourseID(),newSection.getTopic(),
                    newSection.getDescription(), newSection.getAuthor().getUserID(), newSection.getDate()));
            rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM c%s WHERE " +
                    "topic = \"%s\" AND description = \"%s\" AND author = \"%s\" AND date = \"%s\";",
                    subject.getCourseID(), newSection.getTopic(), newSection.getDescription(), newSection.getAuthor().getUserID(), newSection.getDate()));
            rest.next();
            newSection.setID(String.valueOf(rest.getInt("ID")));
            rest.close();
            DbUtil.dbConnect();

            File sourceFolder = new File(String.format("course resources/%s/r%s",
                    subject.getCourseID(), newSection.getID()));
            sourceFolder.mkdir();

            DbUtil.setDatabase("sms_courses_resources_attachments");
            DbUtil.dbExecuteUpdate(String.format("CREATE TABLE c%s_r%s (" +
                    "ID INT(11) UNSIGNED AUTO_INCREMENT PRIMARY KEY, " +
                    "attachment_text VARCHAR(250), " +
                    "attachment_file VARCHAR(200), " +
                    "attachment_size DOUBLE " +
                    ");", subject.getCourseID(), newSection.getID()));

            for (File attachment:sectionFiles){
                DbUtil.dbExecuteUpdate(String.format("INSERT INTO c%s_r%s (attachment_text, attachment_file, attachment_size) VALUES " +
                        "(\"%s\", \"%s\", %.3f);", subject.getCourseID(), newSection.getID(), attachment.getName().substring(0, attachment.getName().indexOf('.')),
                        attachment.getName(), (double) Files.size(Path.of(attachment.getAbsolutePath()))/1024));
                rest = DbUtil.dbExecuteQuery(String.format("SELECT * FROM c%s_r%s WHERE " +
                        "attachment_file = \"%s\";", subject.getCourseID(), newSection.getID(), attachment.getName()));
                rest.next();
                Attachment newAttachment = new Attachment();
                newAttachment.setID(String.valueOf(rest.getInt("ID")));
                rest.close();
                DbUtil.dbConnect();
                File newFile = new File(String.format("course resources/%s/r%s/%s_%s",
                        subject.getCourseID(), newSection.getID(), newAttachment.getID(), attachment.getName()));

                newFile.createNewFile();

                FileChannel src = new FileInputStream(attachment.getAbsolutePath()).getChannel();
                FileChannel dest = new FileOutputStream(newFile.getAbsolutePath()).getChannel();

                try {
                    dest.transferFrom(src, 0, src.size());
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    src.close();
                    dest.close();
                }

            }

            sectionTopic.setText("");
            sectionDescription.setText("");
            sectionFiles.clear();

            addAllResources();


        }
    }


    @FXML
    protected void onAddFilesButton(ActionEvent event){

    }

    @FXML
    protected void onAssignButton(ActionEvent event){

    }




}
