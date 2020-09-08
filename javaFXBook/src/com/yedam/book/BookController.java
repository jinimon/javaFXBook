package com.yedam.book;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class BookController implements Initializable {
	@FXML
	TableView<Book> tableView;
	@FXML
	Button btnAdd, btnNext, btnPrev, btnDelete;

	ObservableList<Book> list;

	Stage primaryStage; // 필드 선언

	int count = 0, num = 0;
	int nextCount = 0;
	int prevCount = 0;
	int selectedNum = 0;

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		TableColumn<Book, ?> tc = tableView.getColumns().get(0);
		tc.setCellValueFactory(new PropertyValueFactory<>("title"));

		tc = tableView.getColumns().get(1);
		tc.setCellValueFactory(new PropertyValueFactory<>("name"));

		tc = tableView.getColumns().get(2);
		tc.setCellValueFactory(new PropertyValueFactory<>("company"));

		tc = tableView.getColumns().get(3);
		tc.setCellValueFactory(new PropertyValueFactory<>("price"));

		// 성적 저장
		list = FXCollections.observableArrayList();

		tableView.setItems(list);
		list.add(new Book("어린왕자", "앙투안 드 생텍쥐페리", "출판사명", 10000));

		// next 버튼
		btnNext.setOnAction(e -> clickBtnNextAction());

		// prv 버튼
		btnPrev.setOnAction(e -> clickBtnPrevAction());

		// 추가 버튼
		btnAdd.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				handleBtnAddAction();
			}
		});

		// 삭제 버튼
		btnDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (tableView.getSelectionModel().isEmpty()) {
					showPopup(" 목록 선택 안됨 ", btnAdd);
				} else {
					handleBtnDeleteAction(tableView.getSelectionModel().getSelectedItems());
//					list.removeAll(tableView.getSelectionModel().getSelectedItems());
				}
			}
		});

		// 클릭시 수정 창
		tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() == 2) { // 2 : double click
					String title = tableView.getSelectionModel().getSelectedItem().getTitle();
					handleModifyAction(title);
				}

			}
		});
	} // end of initialize

	private void clickBtnNextAction() {
		tableView.getSelectionModel().selectNext();
		count = tableView.getSelectionModel().getFocusedIndex();
		if (nextCount == count) {
			tableView.getSelectionModel().selectFirst();
		}
		nextCount = count;
	}

	private void clickBtnPrevAction() {
		tableView.getSelectionModel().selectPrevious();
		num = tableView.getSelectionModel().getFocusedIndex();
		if (prevCount == num) {
			tableView.getSelectionModel().selectLast();
		}
		prevCount = num;
	}

	public void handleModifyAction(String title) {
		Stage stage = new Stage(StageStyle.UTILITY);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(primaryStage);

		try {
			Parent parent = FXMLLoader.load(getClass().getResource("BookUpdate.fxml"));

			Scene scene = new Scene(parent);
			stage.setScene(scene);
			stage.show();

			Button btnUpdate = (Button)parent.lookup("#btnUpdate");
			Button btnUpdateCancel = (Button)parent.lookup("#btnUpdateCancel");
			
			TextField ttitle = (TextField) parent.lookup("#tTitle");
			TextField tname = (TextField) parent.lookup("#tName");
			TextField tcompany = (TextField) parent.lookup("#tCompany");
			TextField tprice = (TextField) parent.lookup("#tPrice");

			// 제목 기준으로 점수 가져오기
			for (Book book : list) {
				if (book.getTitle().equals(title)) {
					ttitle.setText(book.getTitle());
					tname.setText(book.getName());
					tcompany.setText(book.getCompany());
					tprice.setText(String.valueOf(book.getPrice()));
				}
			}

			ttitle.setEditable(false);

			btnUpdate.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).getTitle().equals(title)) {
							Book book = new Book(title, tname.getText(), tcompany.getText(),
									Integer.parseInt(tprice.getText()));
							list.set(i, book);
						}
					}
					stage.close();
				}
			});

			btnUpdateCancel.setOnAction(e -> stage.close());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// AddForm.fxml로 넘어가야함
	public void handleBtnAddAction() {
		// 윈도우 스타일
		Stage stage = new Stage(StageStyle.UTILITY);
		stage.initModality(Modality.WINDOW_MODAL);
		// btnAdd이 있는 윈도우를 지정하고싶다.
		stage.initOwner(btnAdd.getScene().getWindow());

		try {
			// Parent : 모든 컨테이너의 상위
			Parent parent = FXMLLoader.load(getClass().getResource("BookAdd.fxml"));

			Scene scene = new Scene(parent);
			stage.setScene(scene);
			stage.show();

			// Add 화면의 컨트롤 사용하기
			Button btnFormAdd = (Button) parent.lookup("#btnFormAdd");
			btnFormAdd.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					// lookup : 입력한 id 값으로 찾아오겠다.
					TextField txtTitle = (TextField) parent.lookup("#txtTitle");
					TextField txtName = (TextField) parent.lookup("#txtName");
					TextField txtCompany = (TextField) parent.lookup("#txtCompany");
					TextField txtPrice = (TextField) parent.lookup("#txtPrice");
					Book book = new Book(txtTitle.getText(), txtName.getText(), txtCompany.getText(),
							Integer.parseInt(txtPrice.getText()));
					list.add(book);
					// null 값일 경우 입력하라는 팝업 띄우기

					stage.close();
				}
			});

			Button btnFormClear = (Button) parent.lookup("#btnFormClear");
			btnFormClear.setOnAction(e -> {
				TextField txtTitle = (TextField) parent.lookup("#txtTitle");
				TextField txtName = (TextField) parent.lookup("#txtName");
				TextField txtCompany = (TextField) parent.lookup("#txtCompany");
				TextField txtPrice = (TextField) parent.lookup("#txtPrice");

				txtTitle.clear();
				txtName.clear();
				txtCompany.clear();
				txtPrice.clear();
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void handleBtnDeleteAction(ObservableList<Book> book) {

		Stage stage = new Stage(StageStyle.UTILITY);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(primaryStage);

		// 레이아웃
		AnchorPane ap = new AnchorPane();
		ap.setStyle("-fx-background-color: #B6CEC7");
		ap.setPrefSize(150, 80);

		Label comment = new Label("삭제하시겠습니까?");
		comment.setLayoutX(28);
		comment.setLayoutY(15);
		Button btn1 = new Button("확인");
		btn1.setLayoutX(20);
		btn1.setLayoutY(43);
		btn1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				list.removeAll(book);
				stage.close();
			}
		});
		Button btn2 = new Button("취소");
		btn2.setLayoutX(90);
		btn2.setLayoutY(43);
		btn2.setOnAction(e -> stage.close());

		ap.getChildren().addAll(comment, btn1, btn2);

		Scene scene = new Scene(ap);
		stage.setScene(scene);
		stage.show();
	}

	public void showPopup(String msg, Button btn) {
		// poppup 타이틀 등록
		HBox hbox = new HBox();
		hbox.setStyle("-fx-background-color:#ffdc7c;");
		hbox.setAlignment(Pos.CENTER);

		Label label = new Label();
		label.setText(msg);

		hbox.getChildren().add(label);

		Popup pop = new Popup(); // 얘도 컨테이너처럼 컨트롤들이 있어야한다.
		pop.getContent().add(hbox);
		pop.setAutoHide(true);

		// primary 윈도우에 있는 컨트롤 아무거나를 기준으로 얘가 등록된 씬을 알아낼수 있다.
		// 그리고 그 씬이 소속된 윈도우 알아내기
		pop.show(btn.getScene().getWindow());
	}
}