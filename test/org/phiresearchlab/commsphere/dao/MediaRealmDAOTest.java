package org.phiresearchlab.commsphere.dao;

import org.junit.Assert;
import org.junit.Test;
import org.phiresearchlab.commsphere.domain.Category;
import org.phiresearchlab.commsphere.domain.MediaRealm;
import org.phiresearchlab.commsphere.domain.Subcategory;
import org.springframework.beans.factory.annotation.Autowired;

public class MediaRealmDAOTest extends BaseDaoTest {

	@Autowired MediaRealmDAO mediaRealmDAO;
	
	@Test
	public void testCreateMediaRealm() {
	    MediaRealm realm = new MediaRealm("Television");
	    realm.addCategory(createCategory(realm, "First Category", 3));
	    realm.addCategory(createCategory(realm, "Second Category", 1));
	    realm.addCategory(createCategory(realm, "Third Category", 2));
	    
	    mediaRealmDAO.persist(realm);
	    
        Assert.assertTrue("The MediaRealm entity should have a valid database ID", realm.getId() > 0);
	}
	
	private Category createCategory(MediaRealm parent, String name, int subcategoryCount) {
	    Category category = new Category(name);
	    
	    for (int i = 0; i < subcategoryCount; i++) {
	        Subcategory sub = new Subcategory("Subcategory " + (i + 1));
	        category.addSubcategory(sub);
	    }
	    
	    return category;
	}
	
}
