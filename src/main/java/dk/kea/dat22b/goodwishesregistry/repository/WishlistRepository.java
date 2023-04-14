package dk.kea.dat22b.goodwishesregistry.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import dk.kea.dat22b.goodwishesregistry.utility.ConnectionManager;
import dk.kea.dat22b.goodwishesregistry.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class WishlistRepository
	{
		@Value("${spring.datasource.url}")
		private String DB_URL;
		@Value("${spring.datasource.username}")
		private String UID;
		@Value("${spring.datasource.password}")
		private String PWD;

		public List<WishListItems> getWishItemsByID(int wishlistid){
			List<WishListItems> wishlistitems=new ArrayList<>();
			try {
				Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
				Statement statement=connection.createStatement();
				final String SQLgetwishes="SELECT * FROM wishlist.wish_list_items WHERE"+ wishlistid+"=wish_list_id";
				ResultSet resultSet=statement.executeQuery(SQLgetwishes);
				while (resultSet.next()){
				int wishListId=resultSet.getInt(1);
				int itemLineId=resultSet.getInt(2);
				String itemName=resultSet.getString(3);
				int itemQTY=resultSet.getInt(4);
				String itemDescription=resultSet.getString(5);
				String itemURL=resultSet.getString(6);
				double itemPrice=resultSet.getDouble(7);
				boolean itemReserved=resultSet.getBoolean(8);
				String itemReservedBy=resultSet.getString(9);
				WishListItems wish =new WishListItems(wishListId,itemLineId,itemName,itemQTY,itemDescription,itemURL,itemPrice,itemReserved,itemReservedBy);
				wishlistitems.add(wish);
				}


			}catch(SQLException e){
				System.out.println("could not query database");
				e.printStackTrace();

			}
			return wishlistitems;
		}
		public WishUser getUserById(int userId){
			WishUser user =new WishUser();
			try{
				Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
				Statement statement=connection.createStatement();
				final String SQL_USER="SELECT * FROM wishlist.wish_user WHERE"+userId+"= user_id";
				ResultSet resultSet=statement.executeQuery(SQL_USER);
				while (resultSet.next()){
					int user_Id=resultSet.getInt(1);
					String userName=resultSet.getString(2);
					String userPassword=resultSet.getString(3);
					 user.setUserId(user_Id);
					 user.setUserName(userName);
					 user.setUserPassword(userPassword);
				}

			}catch(SQLException e){
				System.out.println("user could not be retrieved");
				e.printStackTrace();
			}
			return user;
		}
		public WishUser loginUser(String username,String userpassword){
			WishUser user=new WishUser();
			try {
				Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
				Statement statement=connection.createStatement();
				final String SQL_LOGIN="SELECT * FROM wishlist.wish_user WHERE user_name="+username+" AND user_password="+userpassword;
				ResultSet resultSet=statement.executeQuery(SQL_LOGIN);
				while (resultSet.next()){
					int user_Id=resultSet.getInt(1);
					String userName=resultSet.getString(2);
					String userPassword=resultSet.getString(3);
					user.setUserId(user_Id);
					user.setUserName(userName);
					user.setUserPassword(userPassword);
				}

			}catch(SQLException e){
				System.out.println("Unable to login - try again");
				e.printStackTrace();
			}
			return user;

		}
		public void addUser(WishUser wishUser){

			try {
				Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
				// check if username+password already exists

				//else create user
				final String SQL_CREATE_USER="INSERT INTO wishlist.wish_user(user_name,user_password) VALUES(?,?)";
				PreparedStatement preparedStatement=connection.prepareStatement(SQL_CREATE_USER);
				preparedStatement.setString(1,wishUser.getUserName());
				preparedStatement.setString(2,wishUser.getUserPassword());
				preparedStatement.executeUpdate();

			}catch (SQLException e){
				System.out.println("could not create user");
				e.printStackTrace();
			}
		}

		public List<WishList> getWishListByUserId(int userId) {
			List<WishList> wishListe = new ArrayList<>();
			try {
				Connection connection = ConnectionManager.getConnection(DB_URL, UID, PWD);
				Statement statement = connection.createStatement();
				final String SQL_QUERY = "SELECT * FROM wishlist.wish_list WHERE user_id = ?";
				ResultSet resultSet = statement.executeQuery(SQL_QUERY);
				while(resultSet.next()) {
					int wish_list_id = resultSet.getInt(1);
					String wish_list_name = resultSet.getString(2);
					String wish_list_occation = resultSet.getString(3);
					int user_id = userId;
					WishList wishlist = new WishList(wish_list_id, wish_list_name, wish_list_occation, user_id);
					wishListe.add(wishlist);
				}
			}
			catch(SQLException e) {
				System.out.println("Cannot connect to database.");
				e.printStackTrace();
			}
			return wishListe;
		}
		public void addWishList(WishList wishlist){
			try{
				Connection connection = ConnectionManager.getConnection(DB_URL, UID, PWD);
				final String CREATE_QUERY = "INSERT INTO wishlist.wish_list( wish_list_name, occation) VALUES(?,?) ";
				PreparedStatement preparedStatement = connection.prepareStatement(CREATE_QUERY);

				//set attributes in prepared statement
				preparedStatement.setString(1, wishlist.getWishListName());
				preparedStatement.setString(2, wishlist.getOccation());
				//execute
				preparedStatement.executeUpdate();
			}
			catch(SQLException e){
				System.out.println("Could not create Wishlist");
				e.printStackTrace();
			}
		}
		public void updateWishList(WishList wishlist){
			final String UPDATE_QUERY = "UPDATE wishlist.wish_list SET wish_list_name = ?, occation = ? WHERE wish_list_id = ?";

			try{
				Connection connection = ConnectionManager.getConnection(DB_URL,UID,PWD);

				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);

				String wish_list_name = wishlist.getWishListName();
				String occation = wishlist.getOccation();
				int wish_list_id = wishlist.getWishListId();

				preparedStatement.setString(1,wish_list_name);
				preparedStatement.setString(2, occation);

				preparedStatement.executeUpdate();
			}
			catch(SQLException e) {
				System.out.println("Could not update wishlist.");
				e.printStackTrace();
			}
		}
		public WishList findWishListById(int id){

			final String FIND_QUERY ="SELECT * FROM wishlist.wish_list WHERE wish_list_id = ?";
			WishList wishlist = new WishList();
			wishlist.setWishListId(id);
			try{

				Connection connection = ConnectionManager.getConnection(DB_URL,UID,PWD);

				PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY);

				preparedStatement.setInt(1, id);

				ResultSet resultSet = preparedStatement.executeQuery();

				resultSet.next();
				String wish_list_name = resultSet.getString(2);
				String occation = resultSet.getString(3);
				wishlist.setWishListName(wish_list_name);
				wishlist.setOccation(occation);
			} catch(SQLException e) {
				System.out.println("could not find wishlist");
				e.printStackTrace();
			}
			return wishlist;
		}

		public void deleteWishListId(int wish_list_id){

			final String DELETE_QUERY = "DELETE FROM wishlist.wish_list WHERE wish_list_id = ?";
			try {
				Connection connection = ConnectionManager.getConnection(DB_URL,UID,PWD);

				PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);

				preparedStatement.setInt(1, wish_list_id);

				preparedStatement.executeUpdate();
			}
			catch(SQLException e) {
				System.out.println("Could not delete wishlist");
				e.printStackTrace();
			}
		}

		public void addWish(WishListItems wishListItems) {

			try {
				Connection connection = ConnectionManager.getConnection(DB_URL, UID, PWD);
				final String SQLcreatewish = "INSERT INTO wishlist.wish_list_items(wish_list_id, item_line_id, item_name, item_QTY, item_description, item_URL, item_price) VALUES(?,?,?,?,?,?,?)";
				PreparedStatement preparedStatement = connection.prepareStatement(SQLcreatewish);

				preparedStatement.setInt(1, wishListItems.getWishListId());
				preparedStatement.setInt(2, wishListItems.getItemLineId());
				preparedStatement.setString(3, wishListItems.getItemName());
				preparedStatement.setInt(4, wishListItems.getItemQTY());
				preparedStatement.setString(5, wishListItems.getItemDescription());
				preparedStatement.setString(6, wishListItems.getItemURL());
				preparedStatement.setDouble(7, wishListItems.getItemPrice());

				preparedStatement.executeUpdate();

			} catch (SQLException e) {
				System.out.println("Could not create new wish");
				e.printStackTrace();
			}
		}
		public void updateWish(WishListItems wishListItems){
			final String UPDATE_QUERY = "UPDATE wishlist.wish_list_items SET item_name=?, item_QTY=?, item_description=?, item_URL=?, item_price=? WHERE item_line_id=?";

			try{
				Connection connection = ConnectionManager.getConnection(DB_URL, UID, PWD);
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
				String itemName = wishListItems.getItemName();
				int itemQTY = wishListItems.getItemQTY();
				String itemDescription = wishListItems.getItemDescription();
				String itemURL = wishListItems.getItemURL();
				double itemPrice = wishListItems.getItemPrice();

				preparedStatement.setString(1, itemName);
				preparedStatement.setInt(2, itemQTY);
				preparedStatement.setString(3,itemDescription);
				preparedStatement.setString(4, itemURL);
				preparedStatement.setDouble(5,itemPrice);

				preparedStatement.executeUpdate();

			}catch(SQLException e){
				System.out.println("Could not update wish");
				e.printStackTrace();
			}
		}
		public WishListItems findWishById(int itemLineId){
			final String FIND_QUERY = "SELECT * FROM wishlist.wish_list_items WHERE item_line_id = ?";

			WishListItems wishListItems = new WishListItems();
			wishListItems.setItemLineId(itemLineId);

			try{

				Connection connection = ConnectionManager.getConnection(DB_URL, UID, PWD);
				PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY);
				preparedStatement.setInt(1, itemLineId);
				ResultSet resultSet = preparedStatement.executeQuery();

				resultSet.next();
				String itemName = resultSet.getString(3);
				int itemQTY = resultSet.getInt(4);
				String itemDescription = resultSet.getString(5);
				String itemURL = resultSet.getString(6);
				double itemPrice = resultSet.getDouble(7);

				wishListItems.setItemName(itemName);
				wishListItems.setItemQTY(itemQTY);
				wishListItems.setItemDescription(itemDescription);
				wishListItems.setItemURL(itemURL);
				wishListItems.setItemPrice(itemPrice);

			}catch (SQLException e){
				System.out.println("Could not find item");
				e.printStackTrace();
			}
			return wishListItems;
		}
		public void deleteById(int itemLineId){
			//SQL-query
			final String DELETE_QUERY = "DELETE FROM wishlist.wish_list_items WHERE item_line_id =?";

			try{
				//Connect til DB
				Connection connection = ConnectionManager.getConnection(DB_URL, UID, PWD);

				//Create statement
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);

				//Set parameter
				preparedStatement.setInt(1, itemLineId);

				//Execute statement
				preparedStatement.executeUpdate();

			}catch (SQLException e){
				System.out.println("Could not delete item");
			}
		}
	}
