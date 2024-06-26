package lk.ijse.gdse.Controller;

/**
 * @author Amil Srinath
 */

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import lk.ijse.gdse.Model.Customer;
import lk.ijse.gdse.Model.TM.CustomerTM;
import lk.ijse.gdse.Repo.CustomerRepo;
import lk.ijse.gdse.Util.Regex;
import lk.ijse.gdse.Util.TextField;
import lk.ijse.gdse.db.DBConnection;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerFormController {
    @FXML
    private JFXTextField txtID;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtContact;

    @FXML
    private JFXTextField txtNIC;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtSalary;

    @FXML
    private JFXTextField txtAge;

    @FXML
    public TableView<CustomerTM> tblCustomer;

    @FXML
    private TableColumn<?, ?> colID;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colContact;

    @FXML
    private TableColumn<?, ?> colNIC;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colSalary;

    @FXML
    private TableColumn<?, ?> colAge;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnUpdate;

    public void initialize() {
        setCellDataFactory();
        loadAllCustomers();
    }

    public void loadAllCustomers(){
        ObservableList<CustomerTM> customerTMS = FXCollections.observableArrayList();

        try {
            List<Customer> customerList = CustomerRepo.getAll();
            for (Customer customer : customerList) {
                customerTMS.add(new CustomerTM(
                        customer.getId(),
                        customer.getName(),
                        customer.getAddress(),
                        customer.getContact(),
                        customer.getNic(),
                        customer.getEmail(),
                        customer.getSalary(),
                        customer.getAge()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tblCustomer.setItems(customerTMS);
    }

    public void setCellDataFactory(){
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException {
        String id = txtID.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String contact = txtContact.getText();
        String nic = txtNIC.getText();
        String email = txtEmail.getText();
        String salary = txtSalary.getText();
        String age = txtAge.getText();

        if (isValied()) {
            boolean isSaved = CustomerRepo.save(new Customer(id, name, address, contact, nic, email, salary, age));

            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Saved!").show();
                clear();
            }
        }
        loadAllCustomers();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException {
        String id = txtID.getText();

        boolean isDeleted = CustomerRepo.delete(id);

        if (isDeleted) {
            new Alert(Alert.AlertType.CONFIRMATION, "Deleted!").show();
            clear();
        }
        loadAllCustomers();
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException {
        String id = txtID.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String contact = txtContact.getText();
        String nic = txtNIC.getText();
        String email = txtEmail.getText();
        String salary = txtSalary.getText();
        String age = txtAge.getText();

        boolean isUpdated = CustomerRepo.update(new Customer(id, name, address, contact, nic, email, salary, age));

        if (isUpdated) {
            new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();
            clear();
        }
        loadAllCustomers();

    }

    public void btnSearch(ActionEvent actionEvent) throws SQLException {
        String id = txtID.getText();
        Customer customer = CustomerRepo.search(id);
        if (customer != null) {
            txtName.setText(customer.getName());
            txtAddress.setText(customer.getAddress());
            txtContact.setText(customer.getContact());
            txtNIC.setText(customer.getNic());
            txtEmail.setText(customer.getEmail());
            txtSalary.setText(customer.getSalary());
            txtAge.setText(customer.getAge());
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clear();
    }

    public void clear(){
        txtID.clear();
        txtName.clear();
        txtAddress.clear();
        txtContact.clear();
        txtNIC.clear();
        txtEmail.clear();
        txtSalary.clear();
        txtAge.clear();
    }

    public void txtCustomerIDOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.ID,txtID);
    }

    public void txtCustomerEmailOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.EMAIL, txtEmail);
    }

    public boolean isValied(){
        if (!Regex.setTextColor(TextField.ID,txtID)) return false;
        if (!Regex.setTextColor(TextField.EMAIL,txtEmail)) return false;
        return true;
    }

    public void btnPrintBillOnAction(ActionEvent actionEvent) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/Report/CustomerBill.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        Map<String,Object> data = new HashMap<>();
        data.put("CustomerID",txtID.getText());
        data.put("Total",getTotal());

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, data, DBConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);

    }

    public String getTotal(){
        return "5000";
    }
}

