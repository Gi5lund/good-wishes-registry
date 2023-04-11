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

		public void addWish(WishListItems wishListItems) {

			try {
				Connection connection = ConnectionManager.getConnection(DB_URL, UID, PWD);
				final String SQLcreatewish = "INSERT INTO wish_list_items(wish_list_id, item_line_id, item_name, item_QTY, item_descreption, item_URL, item_price) VALUES(?,?,?,?,?,?,?)";
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
	}
