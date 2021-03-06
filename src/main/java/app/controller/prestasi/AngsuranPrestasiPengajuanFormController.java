package app.controller.prestasi;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;
import org.controlsfx.dialog.ExceptionDialog;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import app.configs.BootFormInitializable;
import app.configs.StringFormatterFactory;
import app.controller.HomeController;
import app.entities.kepegawaian.uang.prestasi.Motor;
import app.entities.master.DataKaryawan;
import app.repositories.RepositoryKaryawan;
import app.repositories.RepositoryPengajuanAngsuranPrestasi;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.TextArea;

@Component
public class AngsuranPrestasiPengajuanFormController implements BootFormInitializable {

    private Motor motor;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SpinnerValueFactory.DoubleSpinnerValueFactory spinnerCicilanValueFactory;
    private SpinnerValueFactory.IntegerSpinnerValueFactory spinnerJumlahCicilanValueFactory;
    private SpinnerValueFactory.DoubleSpinnerValueFactory spinnerUangMukaValueFactory;
    private ValidationSupport validation;
    private ApplicationContext springContext;

    @FXML
    private TableView<DataKaryawan> tableView;
    @FXML
    private TableColumn<DataKaryawan, String> columnNik;
    @FXML
    private TableColumn<DataKaryawan, String> columnNama;
    @FXML
    private TextField txtMerek;
    @FXML
    private Spinner<Double> txtCicilan;
    @FXML
    private Spinner<Integer> txtJumlahCicilan;
    @FXML
    private Spinner<Double> txtUangMuka;
    @FXML
    private Button btnSave;
    @FXML
    private TextField txtKarywan;
    @FXML
    private TextField txtNik;
    @FXML
    private Label nominalCicilan;
    @FXML
    private Label jumlahCicilan;
    @FXML
    private Label totalPengeluaran;
    @FXML
    private Label totalUangMuka;
    @FXML
    private CheckBox checkValid;
    @FXML
    private TextField txtGajiPokok;
    @FXML
    private TextField txtHireDate;
    @FXML
    private TextField txtHireLongDate;
    @FXML
    private TextField txtJabatan;
    @FXML
    private TextField txtPlatNo;
    @FXML
    private TextField txtJenisMotor;
    @FXML
    private TextField txtWarna;
    @FXML
    private TextField txtNoRangka;
    @FXML
    private TextField txtNoMesin;
    @FXML
    private TextField txtNamaDealer;
    @FXML
    private TextArea txtAlamatDealer;

    @Autowired
    private RepositoryPengajuanAngsuranPrestasi serviceMotor;
    @Autowired
    private RepositoryKaryawan serviceKaryawan;
    @Autowired
    private StringFormatterFactory stringFormater;
    @Autowired
    private HomeController homeController;

    private void clearFields() {
        txtKarywan.clear();
        txtNik.clear();
        txtMerek.clear();
        txtHireDate.clear();
        txtGajiPokok.setText(stringFormater.getCurrencyFormate(0));
        txtJabatan.clear();
        txtHireLongDate.clear();
        this.checkValid.setSelected(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.txtGajiPokok.setText(stringFormater.getCurrencyFormate(0));
        this.totalUangMuka.setText(this.stringFormater.getCurrencyFormate(0));
        this.nominalCicilan.setText(this.stringFormater.getCurrencyFormate(0));
        this.jumlahCicilan.setText(this.stringFormater.getNumberIntegerOnlyFormate(0));
        this.totalPengeluaran.setText(this.stringFormater.getCurrencyFormate(0));

        this.btnSave.setDisable(true);

        this.spinnerCicilanValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0D, 0D);
        this.spinnerJumlahCicilanValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0);
        this.spinnerUangMukaValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0D, 0D);

        this.txtUangMuka.getEditor().setAlignment(Pos.CENTER_RIGHT);
        this.txtUangMuka.setValueFactory(spinnerUangMukaValueFactory);
        this.txtUangMuka.setDisable(true);
        this.txtUangMuka.getValueFactory().valueProperty().addListener((b, old, value) -> {
            this.totalUangMuka.setText(this.stringFormater.getCurrencyFormate(value));
        });

        this.txtCicilan.getEditor().setAlignment(Pos.CENTER_RIGHT);
        this.txtCicilan.setValueFactory(spinnerCicilanValueFactory);
        this.txtCicilan.setDisable(true);
        this.txtCicilan.getValueFactory().valueProperty().addListener((d, old, value) -> {
            this.nominalCicilan.setText(this.stringFormater.getCurrencyFormate(value));
            this.totalPengeluaran.setText(
                    this.stringFormater.getCurrencyFormate(value * this.txtJumlahCicilan.getValueFactory().getValue()));
        });

        this.txtJumlahCicilan.getEditor().setAlignment(Pos.CENTER_RIGHT);
        this.txtJumlahCicilan.setValueFactory(spinnerJumlahCicilanValueFactory);
        this.txtJumlahCicilan.setDisable(true);
        this.txtJumlahCicilan.getValueFactory().valueProperty().addListener((d, old, value) -> {
            this.jumlahCicilan.setText(this.stringFormater.getNumberIntegerOnlyFormate(value));
            this.totalPengeluaran.setText(
                    this.stringFormater.getCurrencyFormate(value * this.txtCicilan.getValueFactory().getValue()));
        });

        this.tableView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends DataKaryawan> values, DataKaryawan oldValue, DataKaryawan newValue) -> {
                    this.btnSave.setOnAction(e -> {
                        doSave(e, newValue);
                    });

                    this.txtJumlahCicilan.setDisable(newValue == null);
                    this.txtCicilan.setDisable(newValue == null);
                    this.txtUangMuka.setDisable(newValue == null);

                    this.txtJumlahCicilan.setEditable(newValue != null);
                    this.txtCicilan.setEditable(newValue != null);
                    this.txtUangMuka.setEditable(newValue != null);

                    if (newValue != null) {
                        txtKarywan.setText(newValue.getNama());
                        txtNik.setText(newValue.getNik());
                        txtGajiPokok.setText(stringFormater.getCurrencyFormate(newValue.getGajiPokok()));
                        txtJabatan.setText(newValue.getJabatan().getNama());
                        txtHireDate.setText(stringFormater
                                .getDateIndonesianFormatter(newValue.getTanggalMulaiKerja().toLocalDate()));
                        txtHireLongDate.setText(stringFormater
                                .getLongDateBetween(newValue.getTanggalMulaiKerja().toLocalDate(), LocalDate.now()));

                        this.spinnerCicilanValueFactory.setMax(newValue.getGajiPokok() + 500000);
                        this.spinnerCicilanValueFactory.setMin(0D);
                        this.spinnerCicilanValueFactory.setAmountToStepBy(50000);
                        this.spinnerCicilanValueFactory.setValue(0D);

                        this.spinnerJumlahCicilanValueFactory.setMax(100);
                        this.spinnerJumlahCicilanValueFactory.setMin(0);
                        this.spinnerJumlahCicilanValueFactory.setAmountToStepBy(5);
                        this.spinnerJumlahCicilanValueFactory.setValue(30);

                        this.spinnerUangMukaValueFactory.setMax(Double.MAX_VALUE);
                        this.spinnerUangMukaValueFactory.setMin(0D);
                        this.spinnerUangMukaValueFactory.setAmountToStepBy(50000);
                        this.spinnerUangMukaValueFactory.setValue(Double.valueOf(3000000));
                    } else {
                        clearFields();

                        this.spinnerCicilanValueFactory.setValue(0D);
                        this.spinnerCicilanValueFactory.setMax(0D);
                        this.spinnerCicilanValueFactory.setMin(0D);
                        this.spinnerCicilanValueFactory.setAmountToStepBy(0D);

                        this.spinnerJumlahCicilanValueFactory.setValue(0);
                        this.spinnerJumlahCicilanValueFactory.setMax(0);
                        this.spinnerJumlahCicilanValueFactory.setMin(0);
                        this.spinnerCicilanValueFactory.setAmountToStepBy(0);

                        this.spinnerUangMukaValueFactory.setValue(0D);
                        this.spinnerUangMukaValueFactory.setMax(0D);
                        this.spinnerUangMukaValueFactory.setMin(0D);
                        this.spinnerUangMukaValueFactory.setAmountToStepBy(0D);
                    }
                });

        this.columnNik.setCellValueFactory(new PropertyValueFactory<DataKaryawan, String>("nip"));
        this.columnNama.setCellValueFactory(new PropertyValueFactory<DataKaryawan, String>("nama"));
        this.initValidator();
    }

    private void doSave(ActionEvent event, DataKaryawan karyawan) {
        try {
            this.motor.setNoPolisi(txtPlatNo.getText());
            this.motor.setTypeMotor(txtJenisMotor.getText());
            this.motor.setWarnaMotor(txtWarna.getText());
            this.motor.setNoRangka(txtNoRangka.getText());
            this.motor.setNoMesin(txtNoMesin.getText());
            this.motor.setDealer(txtNamaDealer.getText());
            this.motor.setAlamatDealer(txtAlamatDealer.getText());
            this.motor.setMerkMotor(txtMerek.getText());
            this.motor.setDp(txtUangMuka.getValueFactory().getValue());
            this.motor.setTotalAngsuran(txtJumlahCicilan.getValueFactory().getValue());
            this.motor.setPembayaran(txtCicilan.getValueFactory().getValue());

            serviceMotor.save(this.motor);
            DataKaryawan dataKaryawan = serviceKaryawan.findOne(karyawan.getIndex());
            dataKaryawan.setNgicilMotor(serviceMotor.findOne(this.motor.getId()));
            this.serviceKaryawan.save(dataKaryawan);

            StringBuilder saveMessage = new StringBuilder("Penggajuan angsuran prestasi karyawan atas nama ");
            saveMessage.append(karyawan.getNama()).append(" dengan NIP ").append(karyawan.getNip())
                    .append(", Berhasil disimpan!");
            Notifications.create().title("Pengajuan angsuran prestasi karyawan").text(saveMessage.toString())
                    .position(Pos.BOTTOM_RIGHT).hideAfter(Duration.seconds(3D)).showInformation();

            initConstuct();
        } catch (Exception e) {
            logger.error("Tidak dapat menyimpan data pengajuan uang prestasi untuk karyawan dengan nama {}",
                    karyawan.getNama(), e);

            StringBuilder errorMessage = new StringBuilder(
                    "Tidak dapat menyimpan data pengajuan angsuran prestasi karyawan atas nama ");
            errorMessage.append(karyawan.getNama()).append(" dengan NIP ").append(karyawan.getNip());
            ExceptionDialog ex = new ExceptionDialog(e);
            ex.setTitle("Pengajuan angsuran prestasi karyawan");
            ex.setHeaderText(errorMessage.toString());
            ex.setContentText(e.getMessage());
            ex.initModality(Modality.APPLICATION_MODAL);
            ex.show();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.springContext = applicationContext;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {

    }

    @Override
    public Node initView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/scenes/inner/prestasi/Pengajuan.fxml"));
        loader.setController(springContext.getBean(this.getClass()));
        return loader.load();
    }

    @Override
    public void setStage(Stage stage) {

    }

    @Override
    public void initConstuct() {
        try {
            this.motor = new Motor();
            this.motor.setTanggalPesan(Date.valueOf(LocalDate.now()));
            this.motor.setNoPolisi("-");
            this.motor.setSetuju(false);
            this.motor.setSudahDiterima(false);
            tableView.getItems().clear();
            for (DataKaryawan data : serviceKaryawan.findAll()) {
                if (data.isGettingCicilanMotor() && data.isAktifBekerja()) {
                    tableView.getItems().add(data);
                }
            }
        } catch (Exception e) {
            logger.error("Tidak dapat mendapatkan data karayawan yang diperbolehkan mengajukan cicilan motor", e);

            ExceptionDialog ex = new ExceptionDialog(e);
            ex.setTitle("Pengajuan angsuran prestasi karyawan");
            ex.setHeaderText("Tidak dapat mendapatkan data karayawan yang diperbolehkan mengajukan cicilan motor");
            ex.setContentText(e.getMessage());
            ex.initModality(Modality.APPLICATION_MODAL);
            ex.show();
        }
    }

    @FXML
    public void doCancel(ActionEvent event) {
        homeController.showWellcome();
    }

    @Override
    public void initValidator() {
        this.validation = new ValidationSupport();
        this.validation.registerValidator(txtNik,
                Validator.createEmptyValidator("Karyawan belum dipilih", Severity.ERROR));
        this.validation.registerValidator(txtMerek,
                Validator.createEmptyValidator("Nama merek kendaraan bermotor", Severity.ERROR));
        this.validation.registerValidator(txtUangMuka.getEditor(),
                (Control c, String value) -> ValidationResult.fromErrorIf(c,
                        "Uang muka minimal harus lebih dari " + stringFormater.getCurrencyFormate(100),
                        Double.valueOf(value) < 100));
        this.validation.registerValidator(txtCicilan.getEditor(),
                (Control c, String value) -> ValidationResult.fromErrorIf(c,
                        "Cicilam motor minimal harus lebih dari " + stringFormater.getCurrencyFormate(200000),
                        Double.valueOf(value) < 200000));
        this.validation.registerValidator(txtJumlahCicilan.getEditor(),
                (Control c, String value) -> ValidationResult.fromErrorIf(c,
                        "Jumlah cicilan minamal harus lebih dari " + stringFormater.getNumberIntegerOnlyFormate(5),
                        Integer.valueOf(value) < 5));
        this.validation.registerValidator(checkValid, (Control c, Boolean value) -> ValidationResult.fromErrorIf(c,
                "Silahkan ceklisk jika data diatas telah sesuia!", value == false));
        this.validation.registerValidator(txtPlatNo,
                Validator.createEmptyValidator("Plat Nomor kendaraan tidak boleh kosong", Severity.ERROR));
        this.validation.registerValidator(txtJenisMotor,
                Validator.createEmptyValidator("Jenis kendaraan tidak boleh kosong", Severity.ERROR));
        this.validation.registerValidator(txtWarna,
                Validator.createEmptyValidator("Warna kendaraan bermotor tidak boleh kosong", Severity.ERROR));
        this.validation.registerValidator(txtNoRangka,
                Validator.createEmptyValidator("Nomor rangka kendaraan bermotor tidak boleh kosong", Severity.ERROR));
        this.validation.registerValidator(txtNoMesin,
                Validator.createEmptyValidator("Nomor mesin kendaraan bermotor tidak boleh kosong", Severity.ERROR));
        this.validation.registerValidator(txtNamaDealer,
                Validator.createEmptyValidator("Nama dealer tidak boleh kosong", Severity.ERROR));
        this.validation.invalidProperty().addListener((o, old, newValue) -> {
            btnSave.setDisable(newValue);
        });

    }

    @FXML
    public void doClear(ActionEvent event) {
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    public void doRefresh(ActionEvent event) {
        initConstuct();
    }

    @Override
    public void initIcons() {
        // TODO Auto-generated method stub

    }

}
