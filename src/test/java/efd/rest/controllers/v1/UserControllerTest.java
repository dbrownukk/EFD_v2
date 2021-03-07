package efd.rest.controllers.v1;

import efd.model.EfdRole;
import efd.rest.repositories.RoleRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;


/*
    @Author david
    @Create 16/02/2021 10:46
*/

public class UserControllerTest {
    @Mock
    RoleRepository roleRepository;
    @InjectMocks
    UserController userController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAll() throws Exception {
        List<EfdRole> result = userController.roleAll();
        Assert.assertEquals(Arrays.<EfdRole>asList(new EfdRole()), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme