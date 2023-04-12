package dk.kea.dat22b.goodwishesregistry.controller;

import dk.kea.dat22b.goodwishesregistry.model.WishListItems;
import dk.kea.dat22b.goodwishesregistry.model.WishUser;
import dk.kea.dat22b.goodwishesregistry.repository.WishlistRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController
	{
		WishlistRepository wishlistRepository;
		public HomeController(WishlistRepository wishlistRepository){
			this.wishlistRepository = wishlistRepository;
		}

		@GetMapping("/createwish")
		public String showCreateWish(){
			return "createwish";
		}

		@PostMapping("/createwish")
		public String createWish(@RequestParam("wishListId") int newWishListId,
								 @RequestParam("itemLineId") int newItemLineId,
								 @RequestParam("itemName") String newItemName,
								 @RequestParam("itemQTY") int newItemQTY,
								 @RequestParam("itemDescreption") String newItemDescreption,
								 @RequestParam("itemURL") String newItemURL,
								 @RequestParam("itemPrice") double newItemPrice) {
			//Laver nyt ønske
			WishListItems newWish = new WishListItems();
			newWish.setWishListId(newWishListId);
			newWish.setItemLineId(newItemLineId);
			newWish.setItemName(newItemName);
			newWish.setItemQTY(newItemQTY);
			newWish.setItemDescription(newItemDescreption);
			newWish.setItemURL(newItemURL);
			newWish.setItemPrice(newItemPrice);
			newWish.setItemReserved(false);
			newWish.getItemReservedBy("");

			//Gem nyt ønske
			wishlistRepository.addWish(newWish);

			return "redirect:/";

		}




		@PostMapping("/login")
		public String login(@RequestParam("username") String username,@RequestParam("pwd") String password){
			if (username.equals("Morten") && password.equals("123")){
				return "testretur";

			}
			return "login";
		}
		@PostMapping("/adduser")
		public String createUser(@RequestParam(userName) String userName,@RequestParam(userPassword) String userPassword){
			WishUser newUser=new WishUser();
			newUser.setUserName(userName);
			newUser.setUserPassword(userPassword);
			wishlistRepository.addUser(newUser);
			return "redirect:/";
		}
	}
