package org.csu.mypetstore.service;

import org.csu.mypetstore.domain.Category;
import org.csu.mypetstore.domain.Item;
import org.csu.mypetstore.domain.Product;
import org.csu.mypetstore.persistence.CategoryMapper;
import org.csu.mypetstore.persistence.ItemMapper;
import org.csu.mypetstore.persistence.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogService
{
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ProductMapper productMapper;

    public List<Category> getCategoryList()
    {
        return categoryMapper.getCategoryList();
    }

    public Category getCategory(String categoryId)
    {
        return categoryMapper.getCategory(categoryId);
    }

    public Product getProduct(String productId)
    {
        return productMapper.getProduct(productId);
    }

    public List<Product> getProductListByCategory(String categoryId)
    {
        return productMapper.getProductListByCategory(categoryId);
    }

    // TODO enable using more than one keyword
    public List<Product> searchProductList(String keyword)
    {
        return productMapper.searchProductList("%" + keyword.toLowerCase() + "%");
    }

    public List<Product> searchProduct(String keyword)
    {
        return productMapper.searchProductList("%" + keyword.toLowerCase() + "%");
    }

    public List<String> searchAllProductList(String username)
    {
        return productMapper.searchAllProductList(username);
    }

    public List<Item> getItemListByProduct(String productId)
    {
        return itemMapper.getItemListByProduct(productId);
    }

    public Item getItem(String itemId)
    {
        return itemMapper.getItem(itemId);
    }

    public boolean isItemInStock(String itemId)
    {
        return itemMapper.getInventoryQuantity(itemId) > 0;
    }

    public int getStockNumByItemId(String itemId){return itemMapper.getStockNumByItemId(itemId);}

    public int updateStockNum(String itemId,int stockNum){itemMapper.updateStockNum(itemId,stockNum); return 1;}

    public List<Item> getAllItem(){return itemMapper.getAllItem();}

    public List<Item> searchItem(String keywords)
    {
        return itemMapper.searchItem("%"+keywords.toLowerCase()+"%");
    }
}
