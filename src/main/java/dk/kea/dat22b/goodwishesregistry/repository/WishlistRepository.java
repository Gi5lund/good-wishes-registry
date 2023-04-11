package dk.kea.dat22b.goodwishesregistry.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
				Connection connection=ConnectionManager.getconnection(DB_URL,UID,PWD);
				Statement statement=connection.createStatement();
				final String SQLgetwishes="SELECT * FROM wishlist.wish_list_items WHERE"+ wishlistid+"=wish_list_id";
				ResultSet resultSet=statement.executeQuery(SQLgetwishes);
				while (resultSet.next()){

				}

			}catch(SQLException e){

			}
		}
	}
