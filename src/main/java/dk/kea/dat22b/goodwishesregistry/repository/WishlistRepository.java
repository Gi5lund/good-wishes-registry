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

		public List<WishListItems> getWishItemsByID(int wishListsId){
			List<WishListItems> wishListItems = new ArrayList<>();
			try {
				Connection connection = ConnectionManager.getConnection(DB_URL,UID,PWD);
				Statement statement = connection.createStatement();

				final String SQL_GETWISHES ="SELECT * FROM wishlist.wish_list_items WHERE wish_list_id ="+ wishListsId;
				ResultSet resultSet=statement.executeQuery(SQL_GETWISHES);

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

				WishListItems wish = new WishListItems(wishListId,itemLineId,itemName,itemQTY,itemDescription,itemURL,itemPrice,itemReserved,itemReservedBy);
				wishListItems.add(wish);
				}
			}catch(SQLException e){
				System.out.println("could not query database");
				e.printStackTrace();
			}
			return wishListItems;
		}

		public WishUser getUserById(int userId){
			WishUser user =new WishUser();
			try{
				Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
				Statement statement=connection.createStatement();

				final String SQL_USER="SELECT * FROM wishlist.wish_user WHERE user_id="+userId;
//				PreparedStatement preparedStatement=connection.prepareStatement(SQL_USER);
//				preparedStatement.setInt(1,userId);
				ResultSet resultSet=statement.executeQuery(SQL_USER);
//				resultSet = preparedStatement.executeQuery(SQL_USER);
				resultSet.next();
					int user_Id=resultSet.getInt(1);
					String userName=resultSet.getString(2);
					String userPassword=resultSet.getString(3);

					 user.setUserId(user_Id);
					 user.setUserName(userName);
					 user.setUserPassword(userPassword);



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
//				Statement statement=connection.createStatement();
				final String SQL_LOGIN="SELECT * FROM wishlist.wish_user WHERE user_name=? AND  user_password =?;";
				PreparedStatement preparedStatement=connection.prepareStatement(SQL_LOGIN);
				preparedStatement.setString(1,username);
				preparedStatement.setString(2,userpassword);
				ResultSet resultSet=preparedStatement.executeQuery();
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
				Statement statement=connection.createStatement();
				final String SQL_QUERY = "SELECT * FROM wishlist.wish_list WHERE user_id ="+userId;

				ResultSet resultSet = statement.executeQuery(SQL_QUERY);
				while(resultSet.next()) {
					int wish_list_id = resultSet.getInt(1);
					String wish_list_name = resultSet.getString(2);
					String wish_list_occation = resultSet.getString(3);

					WishList wishlist = new WishList(wish_list_id, wish_list_name, wish_list_occation, userId);
					wishListe.add(wishlist);
				}
			}
			catch(SQLException e) {
				System.out.println("Cannot connect to database.");
				e.printStackTrace();
			}
			return wishListe;
		}
		public void addWishList(WishList wishlist, int userID){
			try{
				Connection connection = ConnectionManager.getConnection(DB_URL, UID, PWD);
				final String CREATE_QUERY = "INSERT INTO wishlist.wish_list( wish_list_name, occation,user_id) VALUES(?,?,?) ";
				PreparedStatement preparedStatement = connection.prepareStatement(CREATE_QUERY);

				//set attributes in prepared statement
				preparedStatement.setString(1, wishlist.getWishListName());
				preparedStatement.setString(2, wishlist.getOccation());
				preparedStatement.setInt(3,userID);
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
				preparedStatement.setInt(3,wish_list_id);

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

//			final String DELETE_QUERY = "DELETE FROM wishlist.wish_list WHERE wish_list_id = ?";
			final String DELETE_QUERY="DELETE FROM wishlist.wish_list_items WHERE wish_list_id =?";
			final String DELETE_WISHLIST= "DELETE FROM wishlist.wish_list WHERE wish_list_id = ?";
			try {
				Connection connection = ConnectionManager.getConnection(DB_URL,UID,PWD);

				PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
				PreparedStatement preparedStatement2=connection.prepareStatement(DELETE_WISHLIST);

				preparedStatement.setInt(1, wish_list_id);
				preparedStatement2.setInt(1, wish_list_id);


				preparedStatement.executeUpdate();
//				preparedStatement2.executeUpdate();
			}
			catch(SQLException e) {
				System.out.println("Could not delete wishlist");
				e.printStackTrace();
			}
		}

		public void addWish(WishListItems wishListItems,int wishListId) {

			try {
				Connection connection = ConnectionManager.getConnection(DB_URL, UID, PWD);
				final String SQLcreatewish = "INSERT INTO wishlist.wish_list_items( wish_list_id, item_name, item_QTY, item_description, item_URL, item_price)  VALUES(?,?,?,?,?,?) ";
				PreparedStatement preparedStatement = connection.prepareStatement(SQLcreatewish);


				preparedStatement.setInt(1, wishListId);
				preparedStatement.setString(2, wishListItems.getItemName());
				preparedStatement.setInt(3, wishListItems.getItemQTY());
				preparedStatement.setString(4, wishListItems.getItemDescription());
				preparedStatement.setString(5, wishListItems.getItemURL());
				preparedStatement.setDouble(6, wishListItems.getItemPrice());
//				preparedStatement.setBoolean(7,wishListItems.isItemReserved());
//				preparedStatement.setString(8,wishListItems.getItemReservedBy());


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
				int item_line_id=wishListItems.getItemLineId();

				preparedStatement.setString(1, itemName);
				preparedStatement.setInt(2, itemQTY);
				preparedStatement.setString(3,itemDescription);
				preparedStatement.setString(4, itemURL);
				preparedStatement.setDouble(5,itemPrice);
				preparedStatement.setInt(6,item_line_id);
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
