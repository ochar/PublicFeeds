/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.application.internal;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import publicfeeds.application.Service;
import publicfeeds.application.internal.jpa.AuthorRepository;
import publicfeeds.application.internal.jpa.ItemCommentRepository;
import publicfeeds.application.internal.jpa.ItemLikeRepository;
import publicfeeds.application.internal.jpa.ItemRepository;
import publicfeeds.application.internal.jpa.UserEventRepository;
import publicfeeds.domain.Author;
import publicfeeds.domain.Item;
import publicfeeds.domain.ItemComment;
import publicfeeds.domain.ItemLike;
import publicfeeds.domain.Media;
import publicfeeds.domain.UserEvent;

/**
 *
 * @author io
 */
@SpringBootTest
public class ServiceSpringImplTest {
	
	@MockBean private ItemRepository itemRepo;
	@MockBean private AuthorRepository authorRepo;
	@MockBean private ItemLikeRepository likeRepo;
	@MockBean private ItemCommentRepository commentRepo;

	@MockBean private UserEventRepository eventRepo;
	
	
	@Autowired private Service service;
	
	
	private final String AUTHOR_ID_1 = "author7890";
	private final String ITEM_ID_1 = "1234";
	
	private final Item ITEM_1 = new Item(ITEM_ID_1, "Title", "urlszzz", 
				new Media("media url", "titles", Collections.EMPTY_LIST), 
				Instant.EPOCH, 
				"insert html here", 
				Instant.EPOCH.plusSeconds(86000),
				new Author(AUTHOR_ID_1, "Mr.Anderson"));
	
	
	private final String AUTHOR_ID_2 = "buggy";
	private final String ITEM_ID_2 = "5678";
	
	private final Item ITEM_2 = new Item(ITEM_ID_2, "no more", "pizzaz", 
				new Media("media url", "nomore", Collections.EMPTY_LIST), 
				Instant.EPOCH, 
				"insert html here", 
				Instant.EPOCH.plusSeconds(86000), 
				new Author(AUTHOR_ID_2, "HOMER"));
	
	
	@Test
	public void getItemByIdTest_Found() {
		given(itemRepo.findById(ITEM_ID_1)).willReturn(Optional.of(ITEM_1));
		
		Optional<Item> foundItem = service.getItemById(ITEM_ID_1);
		
		assertThat(foundItem).isPresent()
				.get()
				.extracting(Item::getAuthor)
				.extracting(Author::getId)
				.matches(id -> AUTHOR_ID_1.equals(id));
	}
	
	@Test
	public void getItemByIdTest_NotFound() {
		given(itemRepo.findById("non existing")).willReturn(Optional.empty());
		
		Optional<Item> foundItem = service.getItemById("non existing");
		
		assertThat(foundItem).isNotPresent();
	}
	
	
	@Test
	public void getAllItemsTest_NotEmpty() {
		given(itemRepo.findAll()).willReturn(Arrays.asList(ITEM_1, ITEM_2));
		
		List<Item> foundItems = service.getAllItems();
		
		assertThat(foundItems).isNotEmpty()
				.hasSize(2)
				.element(1)
				.extracting(Item::getAuthor)
				.extracting(Author::getId)
				.matches(id -> AUTHOR_ID_2.equals(id));
	}
	
	@Test
	public void getAllItemsTest_Empty() {
		given(itemRepo.findAll()).willReturn(Collections.emptyList());
		
		List<Item> foundItems = service.getAllItems();
		
		assertThat(foundItems).isEmpty();
	}
	
	
	@Test
	public void countItemsTest_NotEmpty() {
		given(itemRepo.count()).willReturn((long)Arrays.asList(ITEM_1, ITEM_2).size());
		
		long foundItems = service.countItems();
		
		assertThat(foundItems).isPositive()
				.isEqualTo(2);
	}
	
	@Test
	public void countItemsTest_Empty() {
		given(itemRepo.count()).willReturn((long)Collections.emptyList().size());
		
		long foundItems = service.countItems();
		
		assertThat(foundItems).isZero();
	}
	
	
	@Test
	public void saveItemTest() {
		given(itemRepo.save(ITEM_1)).willReturn(ITEM_1);
		
		Item savedItem = service.saveItem(ITEM_1);
		
		assertThat(savedItem).isNotNull()
				.extracting(Item::getId)
				.isNotNull()
				.matches(id -> id.trim().isEmpty() == false);
	}
	
	@Test
	public void saveItemTest_Null() {
		given(itemRepo.save(null)).willThrow(IllegalArgumentException.class);

		assertThatThrownBy(() -> service.saveItem(null))
				.isExactlyInstanceOf(IllegalArgumentException.class);
	}
	
	
	@Test
	public void saveItemsTest() {
		given(itemRepo.save(ITEM_1)).willReturn(ITEM_1);
		given(itemRepo.save(ITEM_2)).willReturn(ITEM_2);
		
		List<Item> savedItems = service.saveItems(Arrays.asList(ITEM_1, ITEM_2));
		
		assertThat(savedItems).isNotEmpty()
				.hasSize(2)
				.element(1)
				.isEqualTo(ITEM_2)
				.extracting(Item::getId)
				.isNotNull()
				.matches(id -> id.trim().isEmpty() == false);
	}
	
	@Test
	public void saveItemsTest_Null() {
		assertThatThrownBy(() -> service.saveItems(null))
				.isExactlyInstanceOf(NullPointerException.class);
	}
	
	@Test
	public void saveItemsTest_ContainsNull() {
		given(itemRepo.save(ITEM_1)).willReturn(ITEM_1);
		given(itemRepo.save(ITEM_2)).willReturn(ITEM_2);
		given(itemRepo.save(null)).willThrow(IllegalArgumentException.class);
		
		assertThatCode(() -> service.saveItems(Arrays.asList(ITEM_1, null)))
				.doesNotThrowAnyException();
		
		List<Item> savedItems = service.saveItems(Arrays.asList(ITEM_1, null));
		
		assertThat(savedItems).isNotEmpty()
				.hasSize(1)
				.element(0)
				.isEqualTo(ITEM_1)
				.extracting(Item::getId)
				.isNotNull()
				.matches(id -> id.trim().isEmpty() == false);
	}
	
	
	@Test
	public void deleteItemByIdsTest() {
		doThrow(IllegalArgumentException.class).when(itemRepo).deleteById(null);
		
		boolean savedItems = service.deleteItemByIds(Arrays.asList(ITEM_ID_1, ITEM_ID_2));
		
		assertThat(savedItems).isTrue();
	}
	
	@Test
	public void deleteItemByIdsTest_Null() {
		doThrow(IllegalArgumentException.class).when(itemRepo).deleteById(null);
		
		assertThatThrownBy(() -> service.deleteItemByIds(null))
				.isExactlyInstanceOf(NullPointerException.class);
	}
	
	@Test
	public void deleteItemByIdsTest_ContainsNull() {
		doThrow(IllegalArgumentException.class).when(itemRepo).deleteById(null);
		
		assertThatCode(() -> service.deleteItemByIds(Arrays.asList(null, ITEM_ID_2)))
				.doesNotThrowAnyException();
	}
	
	
	@Test
	public void deleteItemsTest() {
		doThrow(IllegalArgumentException.class).when(itemRepo).deleteInBatch(null);
		
		boolean savedItems = service.deleteItems(Arrays.asList(ITEM_1, ITEM_2));
		
		assertThat(savedItems).isTrue();
	}
	
	@Test
	public void deleteItemsTest_Null() {
		doThrow(IllegalArgumentException.class).when(itemRepo).deleteInBatch(null);
		
		assertThatThrownBy(() -> service.deleteItems(null))
				.isExactlyInstanceOf(IllegalArgumentException.class);
	}
	
	@Test
	public void deleteItemsTest_ContainsNull() {
		doThrow(IllegalArgumentException.class).when(itemRepo).deleteInBatch(null);
		
		doAnswer(invocation -> {
			List<Item> arg = invocation.getArgument(0);
			if (arg.contains(null)) {
				throw new IllegalArgumentException();
			}
			return null;
		}).when(itemRepo).deleteInBatch(ArgumentMatchers.any(List.class));
		
		assertThatThrownBy(() -> service.deleteItems(Arrays.asList(null, ITEM_2)))
				.isExactlyInstanceOf(IllegalArgumentException.class);
	}
	
	
	@Test
	public void deleteAllItemsTest() {
		boolean savedItems = service.deleteAllItems();
		
		assertThat(savedItems).isTrue();
	}
	
	
	@Test
	public void getItemsByAuthorTest_Found() {
		given(itemRepo.findByAuthorId(AUTHOR_ID_1)).willReturn(Arrays.asList(ITEM_1));
		given(itemRepo.findByAuthorId(AUTHOR_ID_2)).willReturn(Arrays.asList(ITEM_2));
		
		List<Item> foundItems = service.getItemsByAuthor(AUTHOR_ID_2);
		
		assertThat(foundItems).isNotEmpty()
				.hasSize(1)
				.first()
				.extracting(Item::getAuthor)
				.extracting(Author::getId)
				.matches(id -> AUTHOR_ID_2.equals(id));
	}
	
	@Test
	public void getItemsByAuthorTest_NotFound() {
		given(itemRepo.findByAuthorId(AUTHOR_ID_1)).willReturn(Arrays.asList(ITEM_1));
		given(itemRepo.findByAuthorId(AUTHOR_ID_2)).willReturn(Arrays.asList(ITEM_2));
		given(itemRepo.findByAuthorId("non existing")).willReturn(Collections.emptyList());
		
		List<Item> foundItems = service.getItemsByAuthor("non existing");
		
		assertThat(foundItems).isEmpty();
	}
	
	
	private final String USERNAME_1 = "Agus";
	private final String USERNAME_2 = "Sapto";
	private final String USERNAME_3 = "Wisnu";
	
	private final ItemLike LIKE_1 = new ItemLike(ITEM_1, USERNAME_1);
	private final ItemLike LIKE_2 = new ItemLike(ITEM_2, USERNAME_1);
	private final ItemLike LIKE_3 = new ItemLike(ITEM_1, USERNAME_2);
	private final ItemLike LIKE_4 = new ItemLike(ITEM_2, USERNAME_2);
	private final ItemLike LIKE_5 = new ItemLike(ITEM_1, USERNAME_3);
	private final ItemLike LIKE_6 = new ItemLike(ITEM_2, USERNAME_3);
	
	@Test
	public void getLikesByItemIdTest_Found() {
		given(likeRepo.findByItemId(ITEM_ID_1)).willReturn(Arrays.asList(LIKE_1, LIKE_3, LIKE_5));
		given(likeRepo.findByItemId(ITEM_ID_2)).willReturn(Arrays.asList(LIKE_2, LIKE_4, LIKE_6));
		given(likeRepo.findByItemId("non existing")).willReturn(Collections.emptyList());
		
		List<ItemLike> foundLikes = service.getLikesByItemId(ITEM_ID_1);
		
		assertThat(foundLikes).isNotEmpty()
				.hasSize(3)
				.element(1)
				.extracting(ItemLike::getUsername)
				.matches(username -> USERNAME_2.equals(username));
	}
	
	@Test
	public void getLikesByItemIdTest_NotFound() {
		given(likeRepo.findByItemId(ITEM_ID_1)).willReturn(Arrays.asList(LIKE_1, LIKE_3, LIKE_5));
		given(likeRepo.findByItemId(ITEM_ID_2)).willReturn(Arrays.asList(LIKE_2, LIKE_4, LIKE_6));
		given(likeRepo.findByItemId("non existing")).willReturn(Collections.emptyList());
		
		List<ItemLike> foundLikes = service.getLikesByItemId("non existing");
		
		assertThat(foundLikes).isEmpty();
	}
	
	@Test
	public void getLikesByUsernameTest_Found() {
		given(likeRepo.findByUsername(USERNAME_1)).willReturn(Arrays.asList(LIKE_1, LIKE_2));
		given(likeRepo.findByUsername(USERNAME_2)).willReturn(Arrays.asList(LIKE_3, LIKE_4));
		given(likeRepo.findByUsername(USERNAME_3)).willReturn(Arrays.asList(LIKE_5, LIKE_6));
		given(likeRepo.findByUsername("non existing")).willReturn(Collections.emptyList());
		
		List<ItemLike> foundLikes = service.getLikesByUsername(USERNAME_2);
		
		assertThat(foundLikes).isNotEmpty()
				.hasSize(2)
				.element(0)
				.extracting(ItemLike::getUsername)
				.matches(username -> USERNAME_2.equals(username));
	}
	
	@Test
	public void getLikesByUsernameTest_NotFound() {
		given(likeRepo.findByUsername(USERNAME_1)).willReturn(Arrays.asList(LIKE_1, LIKE_2));
		given(likeRepo.findByUsername(USERNAME_2)).willReturn(Arrays.asList(LIKE_3, LIKE_4));
		given(likeRepo.findByUsername(USERNAME_3)).willReturn(Arrays.asList(LIKE_5, LIKE_6));
		given(likeRepo.findByUsername("non existing")).willReturn(Collections.emptyList());
		
		List<ItemLike> foundLikes = service.getLikesByUsername("non existing");
		
		assertThat(foundLikes).isEmpty();
	}
	
	@Test
	public void likeAnItemTest_ItemFound() {
		given(itemRepo.findById(ITEM_ID_1)).willReturn(Optional.of(ITEM_1));
		given(likeRepo.findByItemId(ITEM_ID_1)).willReturn(Arrays.asList(LIKE_1, LIKE_3, LIKE_5));
		
		doAnswer((Answer<ItemLike>) 
				invocation -> {
					ItemLike arg = invocation.getArgument(0);
					return arg;
				}).when(likeRepo).save(ArgumentMatchers.any(ItemLike.class));
		
		ItemLike like = service.likeAnItem(ITEM_ID_1, "NEW USER");
		
		assertThat(like).isNotNull()
				.matches(l -> "NEW USER".equals(l.getUsername()));
	}
	
	@Test
	public void likeAnItemTest_ItemFound_LikeExist() {
		given(itemRepo.findById(ITEM_ID_1)).willReturn(Optional.of(ITEM_1));
		given(likeRepo.findByItemId(ITEM_ID_1)).willReturn(Arrays.asList(LIKE_1, LIKE_3, LIKE_5));
		
		ItemLike like = service.likeAnItem(ITEM_ID_1, USERNAME_1);
		
		assertThat(like).isNotNull()
				.isEqualTo(LIKE_1)
				.extracting(ItemLike::getUsername)
				.isEqualTo(USERNAME_1);
	}
	
	@Test
	public void likeAnItemTest_ItemNotFound() {
		given(itemRepo.findById(ITEM_ID_1)).willReturn(Optional.empty());
		
		ItemLike like = service.likeAnItem(ITEM_ID_1, USERNAME_1);
		
		assertThat(like).isNull();
	}
	
	@Test
	public void unlikeAnItemTest_ItemFound_LikeNotExist() {
		given(itemRepo.findById(ITEM_ID_1)).willReturn(Optional.of(ITEM_1));
		given(likeRepo.findByItemId(ITEM_ID_1)).willReturn(Arrays.asList(LIKE_1, LIKE_3, LIKE_5));
		
		boolean success = service.unlikeAnItem(ITEM_ID_1, USERNAME_2);
		
		assertThat(success).isTrue();
	}
	
	@Test
	public void unlikeAnItemTest_ItemFound_LikeExist() {
		given(itemRepo.findById(ITEM_ID_1)).willReturn(Optional.of(ITEM_1));
		given(likeRepo.findByItemId(ITEM_ID_1)).willReturn(Arrays.asList(LIKE_1, LIKE_3, LIKE_5));
		
		boolean success = service.unlikeAnItem(ITEM_ID_1, USERNAME_1);
		
		assertThat(success).isTrue();
	}
	
	@Test
	public void unlikeAnItemTest_ItemNotFound() {
		given(itemRepo.findById(ITEM_ID_1)).willReturn(Optional.empty());
		
		boolean success = service.unlikeAnItem(ITEM_ID_1, USERNAME_1);
		
		assertThat(success).isFalse();
	}
	
	
	private final String COMM_1 = "Noice!";
	private final String COMM_2 = "bit blurry";
	private final String COMM_3 = "autofocus too quick!?!";
	
	private final ItemComment COMMENT_1 = new ItemComment(COMM_1, ITEM_1, USERNAME_1);
	private final ItemComment COMMENT_2 = new ItemComment(COMM_2, ITEM_2, USERNAME_1);
	private final ItemComment COMMENT_3 = new ItemComment(COMM_3, ITEM_2, USERNAME_1);
	
	@Test
	public void getCommentsByItemIdTest_Found() {
		given(commentRepo.findByItemId(ITEM_ID_1)).willReturn(Arrays.asList(COMMENT_1));
		given(commentRepo.findByItemId(ITEM_ID_2)).willReturn(Arrays.asList(COMMENT_2, COMMENT_3));
		given(commentRepo.findByItemId("non existing")).willReturn(Collections.emptyList());
		
		List<ItemComment> foundComments = service.getCommentsByItemId(ITEM_ID_1);
		
		assertThat(foundComments).isNotEmpty()
				.hasSize(1)
				.first()
				.extracting(ItemComment::getItem)
				.matches(item -> ITEM_1.equals(item));
	}
	
	@Test
	public void getCommentsByItemIdTest_NotFound() {
		given(commentRepo.findByItemId(ITEM_ID_1)).willReturn(Arrays.asList(COMMENT_1));
		given(commentRepo.findByItemId(ITEM_ID_2)).willReturn(Arrays.asList(COMMENT_2, COMMENT_3));
		given(commentRepo.findByItemId("non existing")).willReturn(Collections.emptyList());
		
		List<ItemComment> foundComments = service.getCommentsByItemId("non existing");
		
		assertThat(foundComments).isEmpty();
	}
	
	@Test
	public void getCommentsByUsernameTest_Found() {
		given(commentRepo.findByUsername(USERNAME_1)).willReturn(Arrays.asList(COMMENT_1, COMMENT_2, COMMENT_3));
		given(commentRepo.findByUsername("non existing")).willReturn(Collections.emptyList());
		
		List<ItemComment> foundComments = service.getCommentsByUsername(USERNAME_1);
		
		assertThat(foundComments).isNotEmpty()
				.hasSize(3)
				.element(2)
				.extracting(ItemComment::getItem)
				.matches(item -> ITEM_2.equals(item));
	}
	
	@Test
	public void getCommentsByUsernameTest_NotFound() {
		given(commentRepo.findByUsername(USERNAME_1)).willReturn(Arrays.asList(COMMENT_1, COMMENT_2, COMMENT_3));
		given(commentRepo.findByUsername("non existing")).willReturn(Collections.emptyList());
		
		List<ItemComment> foundComments = service.getCommentsByUsername("non existing");
		
		assertThat(foundComments).isEmpty();
	}
	
	@Test
	public void postCommentToItemTest_ItemFound() {
		given(itemRepo.findById(ITEM_ID_2)).willReturn(Optional.of(ITEM_2));
		
		doAnswer((Answer<ItemComment>) 
				invocation -> {
					ItemComment arg = invocation.getArgument(0);
					return arg;
				}).when(commentRepo).save(ArgumentMatchers.any(ItemComment.class));
		
		ItemComment comment = service.postCommentToItem("sample comment", ITEM_ID_2, USERNAME_1);
		
		assertThat(comment).isNotNull()
				.matches(c -> "sample comment".equals(c.getContent()))
				.matches(c -> USERNAME_1.equals(c.getUsername()))
				.matches(c -> ITEM_ID_2.equals(c.getItem().getId()));
	}
	
	@Test
	public void postCommentToItemTest_ItemNotFound() {
		given(itemRepo.findById(ITEM_ID_2)).willReturn(Optional.empty());
		
		ItemComment comment = service.postCommentToItem("sample comment", ITEM_ID_2, USERNAME_1);
		
		assertThat(comment).isNull();
	}
	
	@Test
	public void updateCommentTest_CommentFound() {
		final ItemComment COMMENT_4 = new ItemComment("before update comm", ITEM_2, USERNAME_1);
		long COMMENT_4_ID = 9876L;
		COMMENT_4.setId(COMMENT_4_ID);
		
		given(commentRepo.findById(COMMENT_4_ID)).willReturn(Optional.of(COMMENT_4));
		
		doAnswer((Answer<ItemComment>) 
				invocation -> {
					ItemComment arg = invocation.getArgument(0);
					return arg;
				}).when(commentRepo).save(ArgumentMatchers.any(ItemComment.class));
		
		ItemComment comment = service.updateComment("sample comment", COMMENT_4_ID);
		
		assertThat(comment).isNotNull()
				.matches(c -> "sample comment".equals(c.getContent()))
				.matches(c -> USERNAME_1.equals(c.getUsername()))
				.matches(c -> ITEM_ID_2.equals(c.getItem().getId()));
	}
	
	@Test
	public void updateCommentTest_CommentNotFound() {
		final ItemComment COMMENT_4 = new ItemComment("before update comm", ITEM_2, USERNAME_1);
		long COMMENT_4_ID = 9876L;
		COMMENT_4.setId(COMMENT_4_ID);
		
		given(commentRepo.findById(COMMENT_4_ID)).willReturn(Optional.empty());
		
		ItemComment comment = service.updateComment("sample comment", COMMENT_4_ID);
		
		assertThat(comment).isNull();
	}
	
	@Test
	public void deleteCommentTest_CommentFound() {
		final ItemComment COMMENT_4 = new ItemComment("before update comm", ITEM_2, USERNAME_1);
		long COMMENT_4_ID = 9876L;
		COMMENT_4.setId(COMMENT_4_ID);
		
		given(commentRepo.findById(COMMENT_4_ID)).willReturn(Optional.of(COMMENT_4));
		
		boolean success = service.deleteComment(COMMENT_4_ID);
		
		assertThat(success).isTrue();
	}
	
	@Test
	public void deleteCommentTest_CommentNotFound() {
		final ItemComment COMMENT_4 = new ItemComment("before update comm", ITEM_2, USERNAME_1);
		long COMMENT_4_ID = 9876L;
		COMMENT_4.setId(COMMENT_4_ID);
		
		given(commentRepo.findById(COMMENT_4_ID)).willReturn(Optional.empty());
		
		boolean success = service.deleteComment(COMMENT_4_ID);
		
		assertThat(success).isFalse();
	}
	
	
	private final UserEvent EV1 = new UserEvent(Instant.EPOCH, USERNAME_1, "like", ITEM_ID_1);
	private final UserEvent EV2 = new UserEvent(Instant.EPOCH, USERNAME_1, "like", ITEM_ID_2);
	private final UserEvent EV3 = new UserEvent(Instant.EPOCH, USERNAME_2, "like", ITEM_ID_1);
	private final UserEvent EV4 = new UserEvent(Instant.EPOCH, USERNAME_2, "like", ITEM_ID_2);
	private final UserEvent EV5 = new UserEvent(Instant.EPOCH, USERNAME_3, "like", ITEM_ID_1);
	private final UserEvent EV6 = new UserEvent(Instant.EPOCH, USERNAME_3, "like", ITEM_ID_2);
	
	private final UserEvent EV7 = new UserEvent(Instant.EPOCH, USERNAME_1, "comment", ITEM_ID_1);
	private final UserEvent EV8 = new UserEvent(Instant.EPOCH, USERNAME_1, "comment", ITEM_ID_2);
	private final UserEvent EV9 = new UserEvent(Instant.EPOCH, USERNAME_1, "comment", ITEM_ID_2);
	
	
	@Test
	public void getUserEventsByItemIdTest_Found() {
		given(eventRepo.findByItemId(ITEM_ID_1)).willReturn(Arrays.asList(EV1, EV3, EV5, EV7));
		given(eventRepo.findByItemId(ITEM_ID_2)).willReturn(Arrays.asList(EV2, EV4, EV6, EV8, EV9));
		given(eventRepo.findByItemId("non existing")).willReturn(Collections.emptyList());
		
		List<UserEvent> foundEvents = service.getUserEventsByItemId(ITEM_ID_1);
		
		assertThat(foundEvents).isNotEmpty()
				.hasSize(4)
				.first()
				.matches(event -> ITEM_ID_1.equals(event.getItemId()))
				.matches(event -> USERNAME_1.equals(event.getUsername()))
				.matches(event -> "like".equals(event.getMessage()));
	}
	
	@Test
	public void getUserEventsByItemIdTest_NotFound() {
		given(eventRepo.findByItemId(ITEM_ID_1)).willReturn(Arrays.asList(EV1, EV3, EV5, EV7));
		given(eventRepo.findByItemId(ITEM_ID_2)).willReturn(Arrays.asList(EV2, EV4, EV6, EV8, EV9));
		given(eventRepo.findByItemId("non existing")).willReturn(Collections.emptyList());
		
		List<UserEvent> foundEvents = service.getUserEventsByItemId("non existing");
		
		assertThat(foundEvents).isEmpty();
	}
	
	@Test
	public void getUserEventsByUsernameTest_Found() {
		given(eventRepo.findByUsername(USERNAME_1)).willReturn(Arrays.asList(EV1, EV2, EV7, EV8, EV9));
		given(eventRepo.findByUsername("non existing")).willReturn(Collections.emptyList());
		
		List<UserEvent> foundEvents = service.getUserEventsByUsername(USERNAME_1);
		
		assertThat(foundEvents).isNotEmpty()
				.hasSize(5)
				.element(2)
				.matches(event -> ITEM_ID_1.equals(event.getItemId()))
				.matches(event -> USERNAME_1.equals(event.getUsername()))
				.matches(event -> "comment".equals(event.getMessage()));
	}
	
	@Test
	public void getUserEventsByUsernameTest_NotFound() {
		given(commentRepo.findByUsername(USERNAME_1)).willReturn(Arrays.asList(COMMENT_1, COMMENT_2, COMMENT_3));
		given(commentRepo.findByUsername("non existing")).willReturn(Collections.emptyList());
		
		List<UserEvent> foundEvents = service.getUserEventsByUsername("non existing");
		
		assertThat(foundEvents).isEmpty();
	}
	
	
}
