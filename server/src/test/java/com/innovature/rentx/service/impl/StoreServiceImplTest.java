// package com.innovature.rentx.service.impl;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.mockito.Mockito.anyByte;
// import static org.mockito.Mockito.mock;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

// import com.innovature.rentx.entity.Store;
// import com.innovature.rentx.entity.User;
// import com.innovature.rentx.exception.BadRequestException;
// import com.innovature.rentx.exception.ConflictException;
// import com.innovature.rentx.form.StoreForm;
// import com.innovature.rentx.repository.StoreRepository;
// import com.innovature.rentx.repository.UserRepository;
// import com.innovature.rentx.util.LanguageUtil;
// import com.innovature.rentx.util.Pager;
// import com.innovature.rentx.view.VendorStoreListView;

// import java.time.LocalDate;
// import java.time.ZoneOffset;
// import java.util.ArrayList;
// import java.util.Date;

// import org.junit.jupiter.api.Disabled;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.test.context.ContextConfiguration;
// import org.springframework.test.context.junit.jupiter.SpringExtension;

// @ContextConfiguration(classes = {StoreServiceImpl.class})
// @ExtendWith(SpringExtension.class)
// class StoreServiceImplTest {
//     @MockBean
//     private LanguageUtil languageUtil;

//     @MockBean
//     private StoreRepository storeRepository;

//     @Autowired
//     private StoreServiceImpl storeServiceImpl;

//     @MockBean
//     private UserRepository userRepository;


//     @Test
//     void testAddStore() {
//         when(languageUtil.getTranslatedText(Mockito.<String>any(), Mockito.<Object[]>any(), Mockito.<String>any()))
//                 .thenReturn("Translated Text");

//         User user = new User();
//         user.setCreatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//         user.setEmail("jane.doe@example.org");
//         user.setId(1);
//         user.setPassword("iloveyou");
//         user.setPhone("6625550144");
//         user.setRole((byte) 'A');
//         user.setStatus((byte) 'A');
//         user.setType((byte) 'A');
//         user.setUpdatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//         user.setUsername("janedoe");

//         Store store = new Store();
//         store.setBuildingName("Building Name");
//         store.setCity("Oxford");
//         store.setCreatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//         store.setId(1);
//         store.setLattitude("Lattitude");
//         store.setLongitude("Longitude");
//         store.setMobile("Mobile");
//         store.setName("Name");
//         store.setPincode("Pincode");
//         store.setRoadName("Road Name");
//         store.setState("MD");
//         store.setStatus((byte) 'A');
//         store.setUpdatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//         store.setUser(user);

//         User user2 = new User();
//         user2.setCreatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//         user2.setEmail("jane.doe@example.org");
//         user2.setId(1);
//         user2.setPassword("iloveyou");
//         user2.setPhone("6625550144");
//         user2.setRole((byte) 'A');
//         user2.setStatus((byte) 'A');
//         user2.setType((byte) 'A');
//         user2.setUpdatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//         user2.setUsername("janedoe");

//         Store store2 = new Store();
//         store2.setBuildingName("Building Name");
//         store2.setCity("Oxford");
//         store2.setCreatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//         store2.setId(1);
//         store2.setLattitude("Lattitude");
//         store2.setLongitude("Longitude");
//         store2.setMobile("Mobile");
//         store2.setName("Name");
//         store2.setPincode("Pincode");
//         store2.setRoadName("Road Name");
//         store2.setState("MD");
//         store2.setStatus((byte) 'A');
//         store2.setUpdatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//         store2.setUser(user2);
//         when(storeRepository.findByNameAndLattitudeAndLongitude(Mockito.<String>any(), Mockito.<String>any(),
//                 Mockito.<String>any())).thenReturn(store);
//         when(storeRepository.save(Mockito.<Store>any())).thenReturn(store2);

//         User user3 = new User();
//         user3.setCreatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//         user3.setEmail("jane.doe@example.org");
//         user3.setId(1);
//         user3.setPassword("iloveyou");
//         user3.setPhone("6625550144");
//         user3.setRole((byte) 'A');
//         user3.setStatus((byte) 'A');
//         user3.setType((byte) 'A');
//         user3.setUpdatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//         user3.setUsername("janedoe");
//         when(userRepository.findByIdAndStatus(Mockito.<Integer>any(), anyByte())).thenReturn(user3);

//         StoreForm form = new StoreForm();
//         form.setBuildingName("Building Name");
//         form.setCity("Oxford");
//         form.setLattitude("Lattitude");
//         form.setLongitude("Longitude");
//         form.setMobile("Mobile");
//         form.setName("Name");
//         form.setPincode("Pincode");
//         form.setRoadName("Road Name");
//         form.setState("MD");
//         assertThrows(ConflictException.class, () -> storeServiceImpl.addStore(form));
//         verify(languageUtil).getTranslatedText(Mockito.<String>any(), Mockito.<Object[]>any(), Mockito.<String>any());
//         verify(storeRepository).findByNameAndLattitudeAndLongitude(Mockito.<String>any(), Mockito.<String>any(),
//                 Mockito.<String>any());
//         verify(userRepository).findByIdAndStatus(Mockito.<Integer>any(), anyByte());
//     }

//     @Test
//     void testAddStore2() {
//         when(languageUtil.getTranslatedText(Mockito.<String>any(), Mockito.<Object[]>any(), Mockito.<String>any()))
//                 .thenReturn("Translated Text");
//         when(storeRepository.findByNameAndLattitudeAndLongitude(Mockito.<String>any(), Mockito.<String>any(),
//                 Mockito.<String>any())).thenThrow(new BadRequestException());
//         when(storeRepository.save(Mockito.<Store>any())).thenThrow(new BadRequestException());

//         User user = new User();
//         user.setCreatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//         user.setEmail("jane.doe@example.org");
//         user.setId(1);
//         user.setPassword("iloveyou");
//         user.setPhone("6625550144");
//         user.setRole((byte) 'A');
//         user.setStatus((byte) 'A');
//         user.setType((byte) 'A');
//         user.setUpdatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//         user.setUsername("janedoe");
//         when(userRepository.findByIdAndStatus(Mockito.<Integer>any(), anyByte())).thenReturn(user);

//         StoreForm form = new StoreForm();
//         form.setBuildingName("Building Name");
//         form.setCity("Oxford");
//         form.setLattitude("Lattitude");
//         form.setLongitude("Longitude");
//         form.setMobile("Mobile");
//         form.setName("Name");
//         form.setPincode("Pincode");
//         form.setRoadName("Road Name");
//         form.setState("MD");
//         assertThrows(BadRequestException.class, () -> storeServiceImpl.addStore(form));
//         verify(storeRepository).findByNameAndLattitudeAndLongitude(Mockito.<String>any(), Mockito.<String>any(),
//                 Mockito.<String>any());
//         verify(userRepository).findByIdAndStatus(Mockito.<Integer>any(), anyByte());
//     }

   
//     @Test
//     void testListStore() {
//         when(storeRepository.countByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any())).thenReturn(1);
//         ArrayList<Store> content = new ArrayList<>();
//         when(storeRepository.findByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any(), Mockito.<PageRequest>any())).thenReturn(new PageImpl<>(content));
//         Pager<VendorStoreListView> actualListStoreResult = storeServiceImpl.listStore("Search", 1, 3, "Sort",
//                 "Direction");
//         assertEquals(1, actualListStoreResult.getCurrentPage());
//         assertEquals(0, actualListStoreResult.getStartIndex());
//         assertEquals(content, actualListStoreResult.getResult());
//         assertEquals(3, actualListStoreResult.getPageSize());
//         assertEquals(10, actualListStoreResult.getDisplayCount());
//         assertEquals(1, actualListStoreResult.getDisplayStart());
//         assertEquals(1, actualListStoreResult.getItemEnd());
//         assertEquals(1, actualListStoreResult.getItemStart());
//         assertEquals(1, actualListStoreResult.getLastPage());
//         assertEquals(1, actualListStoreResult.getDisplayEnd());
//         assertEquals(1, actualListStoreResult.getNumItems());
//         verify(storeRepository).countByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any());
//         verify(storeRepository).findByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any(), Mockito.<PageRequest>any());
//     }

  
//     @Test
//     void testListStore2() {
//         when(storeRepository.countByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any())).thenThrow(new ConflictException("Just cause"));
//         when(storeRepository.findByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any(), Mockito.<PageRequest>any()))
//                 .thenReturn(new PageImpl<>(new ArrayList<>()));
//         assertThrows(ConflictException.class, () -> storeServiceImpl.listStore("Search", 1, 3, "Sort", "Direction"));
//         verify(storeRepository).countByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any());
//         verify(storeRepository).findByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any(), Mockito.<PageRequest>any());
//     }

//     @Test
//     void testListStore3() {
//         when(storeRepository.countByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any())).thenReturn(3);
//         ArrayList<Store> content = new ArrayList<>();
//         when(storeRepository.findByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any(), Mockito.<PageRequest>any())).thenReturn(new PageImpl<>(content));
//         Pager<VendorStoreListView> actualListStoreResult = storeServiceImpl.listStore("Search", 1, 3, "Sort",
//                 "Direction");
//         assertEquals(1, actualListStoreResult.getCurrentPage());
//         assertEquals(0, actualListStoreResult.getStartIndex());
//         assertEquals(content, actualListStoreResult.getResult());
//         assertEquals(3, actualListStoreResult.getPageSize());
//         assertEquals(10, actualListStoreResult.getDisplayCount());
//         assertEquals(1, actualListStoreResult.getDisplayStart());
//         assertEquals(3, actualListStoreResult.getItemEnd());
//         assertEquals(1, actualListStoreResult.getItemStart());
//         assertEquals(1, actualListStoreResult.getLastPage());
//         assertEquals(1, actualListStoreResult.getDisplayEnd());
//         assertEquals(3, actualListStoreResult.getNumItems());
//         verify(storeRepository).countByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any());
//         verify(storeRepository).findByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any(), Mockito.<PageRequest>any());
//     }


//     @Test
//     void testListStore4() {
//         when(storeRepository.countByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any())).thenReturn(-4);
//         ArrayList<Store> content = new ArrayList<>();
//         when(storeRepository.findByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any(), Mockito.<PageRequest>any())).thenReturn(new PageImpl<>(content));
//         Pager<VendorStoreListView> actualListStoreResult = storeServiceImpl.listStore("Search", 1, 3, "Sort",
//                 "Direction");
//         assertEquals(1, actualListStoreResult.getCurrentPage());
//         assertEquals(0, actualListStoreResult.getStartIndex());
//         assertEquals(content, actualListStoreResult.getResult());
//         assertEquals(3, actualListStoreResult.getPageSize());
//         assertEquals(10, actualListStoreResult.getDisplayCount());
//         assertEquals(1, actualListStoreResult.getDisplayStart());
//         assertEquals(0, actualListStoreResult.getItemEnd());
//         assertEquals(1, actualListStoreResult.getItemStart());
//         assertEquals(1, actualListStoreResult.getLastPage());
//         assertEquals(1, actualListStoreResult.getDisplayEnd());
//         assertEquals(0, actualListStoreResult.getNumItems());
//         verify(storeRepository).countByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any());
//         verify(storeRepository).findByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any(), Mockito.<PageRequest>any());
//     }


//     @Test
//     void testListStore5() {
//         User user = new User();
//         user.setCreatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//         user.setEmail("jane.doe@example.org");
//         user.setId(1);
//         user.setPassword("iloveyou");
//         user.setPhone("6625550144");
//         user.setRole((byte) 'A');
//         user.setStatus((byte) 'A');
//         user.setType((byte) 'A');
//         user.setUpdatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//         user.setUsername("janedoe");

//         Store store = new Store();
//         store.setBuildingName("asc");
//         store.setCity("Oxford");
//         store.setCreatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//         store.setId(1);
//         store.setLattitude("asc");
//         store.setLongitude("asc");
//         store.setMobile("asc");
//         store.setName("asc");
//         store.setPincode("asc");
//         store.setRoadName("asc");
//         store.setState("MD");
//         store.setStatus((byte) 'A');
//         store.setUpdatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//         store.setUser(user);

//         ArrayList<Store> content = new ArrayList<>();
//         content.add(store);
//         PageImpl<Store> pageImpl = new PageImpl<>(content);
//         when(storeRepository.countByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any())).thenReturn(1);
//         when(storeRepository.findByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any(), Mockito.<PageRequest>any())).thenReturn(pageImpl);
//         Pager<VendorStoreListView> actualListStoreResult = storeServiceImpl.listStore("Search", 1, 3, "Sort",
//                 "Direction");
//         assertEquals(1, actualListStoreResult.getCurrentPage());
//         assertEquals(0, actualListStoreResult.getStartIndex());
//         assertEquals(1, actualListStoreResult.getResult().size());
//         assertEquals(3, actualListStoreResult.getPageSize());
//         assertEquals(10, actualListStoreResult.getDisplayCount());
//         assertEquals(1, actualListStoreResult.getDisplayStart());
//         assertEquals(1, actualListStoreResult.getItemEnd());
//         assertEquals(1, actualListStoreResult.getItemStart());
//         assertEquals(1, actualListStoreResult.getLastPage());
//         assertEquals(1, actualListStoreResult.getDisplayEnd());
//         assertEquals(1, actualListStoreResult.getNumItems());
//         verify(storeRepository).countByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any());
//         verify(storeRepository).findByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any(), Mockito.<PageRequest>any());
//     }


//     @Test
//     void testListStore6() {
//         User user = new User();
//         user.setCreatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//         user.setEmail("jane.doe@example.org");
//         user.setId(1);
//         user.setPassword("iloveyou");
//         user.setPhone("6625550144");
//         user.setRole((byte) 'A');
//         user.setStatus((byte) 'A');
//         user.setType((byte) 'A');
//         user.setUpdatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//         user.setUsername("janedoe");

//         Store store = new Store();
//         store.setBuildingName("asc");
//         store.setCity("Oxford");
//         store.setCreatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//         store.setId(1);
//         store.setLattitude("asc");
//         store.setLongitude("asc");
//         store.setMobile("asc");
//         store.setName("asc");
//         store.setPincode("asc");
//         store.setRoadName("asc");
//         store.setState("MD");
//         store.setStatus((byte) 'A');
//         store.setUpdatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//         store.setUser(user);

//         User user2 = new User();
//         user2.setCreatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//         user2.setEmail("john.smith@example.org");
//         user2.setId(2);
//         user2.setPassword("asc");
//         user2.setPhone("8605550118");
//         user2.setRole((byte) 1);
//         user2.setStatus((byte) 1);
//         user2.setType((byte) 1);
//         user2.setUpdatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//         user2.setUsername("asc");

//         Store store2 = new Store();
//         store2.setBuildingName("Building Name");
//         store2.setCity("London");
//         store2.setCreatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//         store2.setId(2);
//         store2.setLattitude("Lattitude");
//         store2.setLongitude("Longitude");
//         store2.setMobile("Mobile");
//         store2.setName("Name");
//         store2.setPincode("Pincode");
//         store2.setRoadName("Road Name");
//         store2.setState("asc");
//         store2.setStatus((byte) 1);
//         store2.setUpdatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//         store2.setUser(user2);

//         ArrayList<Store> content = new ArrayList<>();
//         content.add(store2);
//         content.add(store);
//         PageImpl<Store> pageImpl = new PageImpl<>(content);
//         when(storeRepository.countByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any())).thenReturn(1);
//         when(storeRepository.findByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any(), Mockito.<PageRequest>any())).thenReturn(pageImpl);
//         Pager<VendorStoreListView> actualListStoreResult = storeServiceImpl.listStore("Search", 1, 3, "Sort",
//                 "Direction");
//         assertEquals(1, actualListStoreResult.getCurrentPage());
//         assertEquals(0, actualListStoreResult.getStartIndex());
//         assertEquals(2, actualListStoreResult.getResult().size());
//         assertEquals(3, actualListStoreResult.getPageSize());
//         assertEquals(10, actualListStoreResult.getDisplayCount());
//         assertEquals(1, actualListStoreResult.getDisplayStart());
//         assertEquals(1, actualListStoreResult.getItemEnd());
//         assertEquals(1, actualListStoreResult.getItemStart());
//         assertEquals(1, actualListStoreResult.getLastPage());
//         assertEquals(1, actualListStoreResult.getDisplayEnd());
//         assertEquals(1, actualListStoreResult.getNumItems());
//         verify(storeRepository).countByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any());
//         verify(storeRepository).findByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any(), Mockito.<PageRequest>any());
//     }

//     @Test
//     @Disabled("TODO: Complete this test")
//     void testListStore7() {

//         when(storeRepository.countByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any())).thenReturn(1);
//         when(storeRepository.findByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any(), Mockito.<PageRequest>any())).thenReturn(null);
//         storeServiceImpl.listStore("Search", 1, 3, "Sort", "Direction");
//     }


//     @Test
//     void testListStore8() {
//         when(storeRepository.countByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any())).thenReturn(1);
//         ArrayList<Store> content = new ArrayList<>();
//         when(storeRepository.findByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any(), Mockito.<PageRequest>any())).thenReturn(new PageImpl<>(content));
//         Pager<VendorStoreListView> actualListStoreResult = storeServiceImpl.listStore("Search", 3, 3, "Sort",
//                 "Direction");
//         assertEquals(1, actualListStoreResult.getCurrentPage());
//         assertEquals(0, actualListStoreResult.getStartIndex());
//         assertEquals(content, actualListStoreResult.getResult());
//         assertEquals(3, actualListStoreResult.getPageSize());
//         assertEquals(10, actualListStoreResult.getDisplayCount());
//         assertEquals(1, actualListStoreResult.getDisplayStart());
//         assertEquals(1, actualListStoreResult.getItemEnd());
//         assertEquals(1, actualListStoreResult.getItemStart());
//         assertEquals(1, actualListStoreResult.getLastPage());
//         assertEquals(1, actualListStoreResult.getDisplayEnd());
//         assertEquals(1, actualListStoreResult.getNumItems());
//         verify(storeRepository).countByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any());
//         verify(storeRepository).findByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any(), Mockito.<PageRequest>any());
//     }

  
//     @Test
//     void testListStore9() {
//         when(languageUtil.getTranslatedText(Mockito.<String>any(), Mockito.<Object[]>any(), Mockito.<String>any()))
//                 .thenReturn("Translated Text");
//         when(storeRepository.countByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any())).thenReturn(1);
//         when(storeRepository.findByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any(), Mockito.<PageRequest>any()))
//                 .thenReturn(new PageImpl<>(new ArrayList<>()));
//         assertThrows(BadRequestException.class, () -> storeServiceImpl.listStore("Search", -4, 3, "Sort", "Direction"));
//         verify(languageUtil).getTranslatedText(Mockito.<String>any(), Mockito.<Object[]>any(), Mockito.<String>any());
//     }

 
//     @Test
//     void testListStore10() {
//         when(languageUtil.getTranslatedText(Mockito.<String>any(), Mockito.<Object[]>any(), Mockito.<String>any()))
//                 .thenThrow(new ConflictException("Just cause"));
//         when(storeRepository.countByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any())).thenReturn(1);
//         when(storeRepository.findByUserIdAndUserRoleAndUserStatusAndNameContainingIgnoreCase(Mockito.<Integer>any(),
//                 anyByte(), anyByte(), Mockito.<String>any(), Mockito.<PageRequest>any()))
//                 .thenReturn(new PageImpl<>(new ArrayList<>()));
//         assertThrows(ConflictException.class, () -> storeServiceImpl.listStore("Search", -4, 3, "Sort", "Direction"));
//         verify(languageUtil).getTranslatedText(Mockito.<String>any(), Mockito.<Object[]>any(), Mockito.<String>any());
//     }


//     @Test
//     @Disabled("TODO: Complete this test")
//     void testList() {

//         Iterable<Store> iterable = mock(Iterable.class);
//         when(iterable.spliterator()).thenReturn(null);
//         when(storeRepository.findAllByStatus(anyByte())).thenReturn(iterable);
//         storeServiceImpl.list();
//     }

   
//     @Test
//     @Disabled("TODO: Complete this test")
//     void testList2() {

//         Iterable<Store> iterable = mock(Iterable.class);
//         when(iterable.spliterator()).thenThrow(new ConflictException("Just cause"));
//         when(storeRepository.findAllByStatus(anyByte())).thenReturn(iterable);
//         storeServiceImpl.list();
//     }

 
//     @Test
//     void testList3() {
//         when(storeRepository.findAllByStatus(anyByte())).thenThrow(new ConflictException("Just cause"));
//         assertThrows(ConflictException.class, () -> storeServiceImpl.list());
//         verify(storeRepository).findAllByStatus(anyByte());
//     }
// }

