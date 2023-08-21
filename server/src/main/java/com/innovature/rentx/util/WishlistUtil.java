package com.innovature.rentx.util;

import com.innovature.rentx.entity.User;
import com.innovature.rentx.entity.Wishlist;
import com.innovature.rentx.exception.BadRequestException;
import com.innovature.rentx.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WishlistUtil {

    private final WishlistRepository wishlistRepository;
    private final LanguageUtil languageUtil;
    private final UserUtil userUtil;

    private static final String UNABLE_PERFORM = "unable.to.perform.this.action";
    private final byte[] wishlistExists = { Wishlist.STATUS.ACTIVE.value, Wishlist.STATUS.DELETED.value };
    private final byte[] activeWishlist = { Wishlist.STATUS.ACTIVE.value };

    public Wishlist checkWishlistExists(Integer productId) {
        User user = userUtil.validateUser(UNABLE_PERFORM);
        Wishlist wishlist = wishlistRepository.findByProductIdAndUserIdAndStatusIn(productId, user, wishlistExists);
         if(wishlist!=null&&wishlist.getStatus()==Wishlist.STATUS.DELETED.value){

            return wishlist;

         } else if (wishlist!=null&&wishlist.getStatus()==Wishlist.STATUS.ACTIVE.value) {
             throw new
                     BadRequestException(languageUtil.getTranslatedText("wishlist.already.added",null,"en"));
         } else{
             return wishlist;
         }
    }

    public Wishlist getWishListId(Integer productId) {

        User user = userUtil.validateUser1(UNABLE_PERFORM);
        if(user!=null) {
            return wishlistRepository.findByProductIdAndUserIdAndStatusIn(productId, user, activeWishlist);
        }
        else{
            return null;
        }
    }

    public Wishlist wishlistExists(Integer id) {
        User user = userUtil.validateUser(UNABLE_PERFORM);
        return wishlistRepository.findByIdAndStatusAndUserId(id, Wishlist.STATUS.ACTIVE.value, user)
                .orElseThrow(() -> new BadRequestException(
                        languageUtil.getTranslatedText("wishlist.item.found", null, "en")));

    }

}
