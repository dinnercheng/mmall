package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("iCartService")
public class CartServiceImpl implements ICartService {

    @Autowired
    CartMapper cartMapper;
    @Autowired
    ProductMapper productMapper;

    public ServerResponse add(Integer userId, Integer productId, Integer count) {

        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if (cart == null) {
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.Cart.CHECKED);
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);
            cartMapper.insert(cartItem);
        } else {
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return  this.list(userId);
    }


    public ServerResponse<CartVo> list(Integer userId){
        CartVo cartVo =  this.getCartVoLimit(userId);
        return  ServerResponse.createBySuccess(cartVo);
    }


    public ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer productId, Integer checked) {
        return null;
    }


    public ServerResponse<CartVo> deleteProduct(Integer userId, String productIds) {
        return null;
    }


    public ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count){

        if(productId == null || count == null){
            return  ServerResponse.createByErrorMessage("parameter error");
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        if(cart != null){
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return ServerResponse.createBySuccess(this.getCartVoLimit(userId));
    }



    public ServerResponse<Integer> getCartProductCount(Integer userId){
        if(userId == null){
            return  ServerResponse.createByError();
       }
        int resultCount = cartMapper.selectCarProductCount(userId);
        return ServerResponse.createBySuccess(resultCount);
    }

    private CartVo getCartVoLimit(Integer userId) {
        CartVo cartVo = new CartVo();
        List<Cart> carts = cartMapper.selectCartByUserId(userId);
        List<CartProductVo> cartProductVos = Lists.newArrayList();

        BigDecimal cartTotalPrice = new BigDecimal("0");

        if (CollectionUtils.isNotEmpty(carts)) {
            for (Cart cartItem : carts) {
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(userId);
                cartProductVo.setProductId(cartItem.getProductId());

                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());

                if (product != null) {
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());

                    int buyLimitCount = 0;
                    //库存数量和购物车中购买数量
                    if(product.getStock().intValue()>cartItem.getQuantity()){
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);

                    }else{
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),
                            cartProductVo.getQuantity().doubleValue()));

                    //计算购物车总价
                    if(cartItem.getChecked() == Const.Cart.CHECKED){
                        cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue()
                                ,cartProductVo.getProductTotalPrice().doubleValue());
                    }
                    cartProductVos.add(cartProductVo);
                }
            }
        }
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVos);
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        cartVo.setAllChecked(getAllCheckedStatus(userId));
        return  cartVo;
    }

    private boolean getAllCheckedStatus(Integer userId){
        if(userId == null){
            return  false;
        }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId)==0;
    }
}
