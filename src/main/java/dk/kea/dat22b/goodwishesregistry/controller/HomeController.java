package dk.kea.dat22b.goodwishesregistry.controller;

import dk.kea.dat22b.goodwishesregistry.model.WishListItems;
import dk.kea.dat22b.goodwishesregistry.repository.WishlistRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

			//Gem nyt ønske
			wishlistRepository.addWish(newWish);

			return "redirect:/";

		}

		@GetMapping("/updatewish/{itemListId}")
		public String showUpdateWish(@PathVariable("itemLineId") int updateWish, Model model){

			WishListItems updateWishListItems = wishlistRepository.findWishById(updateWish);

			model.addAttribute("wishListItem", updateWishListItems);

			return "updatewish";
		}

		@PostMapping("/updatewish")
		public String updateWishItem(@RequestParam("wishListId") int updateWishListId,
									 @RequestParam("itemLineId") int updateItemLineId,
								 	 @RequestParam("itemName") String updateItemName,
								 	 @RequestParam("itemQTY") int updateItemQTY,
									 @RequestParam("itemDescreption") String updateItemDescreption,
									 @RequestParam("itemURL") String updateItemURL,
									 @RequestParam("itemPrice") double updateItemPrice,
									 @RequestParam("itemReserved") boolean updateItemReserved,
									 @RequestParam("itemReservedBy") String updateItemReservedBy){

			WishListItems updateWishItem = new WishListItems(updateWishListId, updateItemLineId, updateItemName, updateItemQTY, updateItemDescreption,
															updateItemURL, 	updateItemPrice, updateItemReserved,updateItemReservedBy);

			wishlistRepository.updateWish(updateWishItem);

			return "/redirect:wishlist";
		}

		@GetMapping("/delete/{itemLineId}")
		public String deleteWish(@PathVariable("itemLineId") int deleteWish){
			//slet fra repository
			wishlistRepository.deleteById(deleteWish);

			//returner til index-siden
			return "redirect:wishlist";
		}


		@PostMapping("/login")
		public String login(@RequestParam("username") String username,@RequestParam("pwd") String password){
			if (username.equals("Morten") && password.equals("123")){
				return "testretur";

			}
			return "login";
		}
		@GetMapping("/jacob")
		public String login(){
			return "jacob";
		}
	}
