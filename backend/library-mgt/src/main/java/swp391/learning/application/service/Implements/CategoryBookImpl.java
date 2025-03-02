package swp391.learning.application.service.Implements;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import swp391.learning.application.service.CategoryBookService;
import swp391.learning.domain.dto.common.ResponseCommon;
import swp391.learning.domain.dto.request.admin.category.AddCategoryRequest;
import swp391.learning.domain.dto.request.admin.category.DeleteCategoryRequest;
import swp391.learning.domain.dto.request.admin.category.UpdateCategoryRequest;
import swp391.learning.domain.dto.response.admin.category.AddCategoryResponse;
import swp391.learning.domain.dto.response.admin.category.DeleteCategoryResponse;
import swp391.learning.domain.dto.response.admin.category.UpdateCategoryResponse;
import swp391.learning.domain.entity.Category;
import swp391.learning.domain.entity.User;
import swp391.learning.domain.enums.ResponseCode;
import swp391.learning.repository.CategoryRepository;
import swp391.learning.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CategoryBookImpl implements CategoryBookService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(CategoryBookImpl.class);

    @Override
    public ResponseCommon<AddCategoryResponse> addCategory(AddCategoryRequest addCategoryRequest) {
        try {
            Category category = categoryRepository.findCategoryByNameCategory(addCategoryRequest.getNameCategory()).orElse(null);
            User user = userRepository.findByEmail(addCategoryRequest.getEmail());
            // if category not null -> tell user
            if (!Objects.isNull(category)) {
                log.debug("Add Category failed: Category already exists");
                return new ResponseCommon<>(ResponseCode.CATEGORY_EXIST, null);
            }
            // if category is null -> new category
            if (Objects.isNull(category)) {
                category = new Category();
            }
            category.setNameCategory(addCategoryRequest.getNameCategory());
            category.setUserCreated(user);
            LocalDateTime localDateTime = LocalDateTime.now();
            category.setUpdatedAt(localDateTime.now());
            // Save category to the database
            Category savedCategory = categoryRepository.save(category);

            // If category is not saved successfully, return a FAIL response
            if (savedCategory == null) {
                log.debug("Add Category failed: Unable to save the category");
                return new ResponseCommon<>(ResponseCode.FAIL, null);
            }
            AddCategoryResponse addCategoryResponse = new AddCategoryResponse();
            addCategoryResponse.setMessage("Add Category successful");
            return new ResponseCommon<>(ResponseCode.SUCCESS, addCategoryResponse);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Add Category failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<UpdateCategoryResponse> updateCategory(UpdateCategoryRequest updateCategoryRequest) {
        try {
            Category category = categoryRepository.findCategoryById(updateCategoryRequest.getCategoryID()).orElse(null);
            User user = userRepository.findByEmail(updateCategoryRequest.getEmail());
            // if category is null -> tell user
            if (Objects.isNull(category)) {
                log.debug("Update Category failed: Category does not exist");
                return new ResponseCommon<>(ResponseCode.CATEGORY_NOT_EXIST, null);
            } else {
                //tạo mảng để lưu dữ liệu mới
                Category categoryUpdate = category;
                categoryUpdate.setNameCategory(updateCategoryRequest.getCategoryUpdate());
                categoryUpdate.setUpdatedAt(LocalDateTime.now());
                categoryUpdate.setDeleted(updateCategoryRequest.isDeleted());
                categoryUpdate.setUserUpdated(user);
                categoryRepository.save(categoryUpdate);
                //update du lieu
                UpdateCategoryResponse updateCategoryResponse = new UpdateCategoryResponse();
                updateCategoryResponse.setMessage("Update Category successful");
                return new ResponseCommon<>(ResponseCode.SUCCESS, updateCategoryResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Update Category failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<DeleteCategoryResponse> deleteCategory(DeleteCategoryRequest deleteCategoryRequest) {
        try {
            Category category = categoryRepository.findCategoryById(deleteCategoryRequest.getCategoryID()).orElse(null);
            User user = userRepository.findByEmail(deleteCategoryRequest.getEmail());
            // if category is null -> tell the user
            if (Objects.isNull(category)) {
                log.debug("Delete Category failed: Category does not exist");
                return new ResponseCommon<>(ResponseCode.CATEGORY_NOT_EXIST, null);
            } else {
                Category categoryUpdate = category;
                categoryUpdate.setDeleted(true);
                categoryUpdate.setUpdatedAt(LocalDateTime.now());
                categoryUpdate.setUserUpdated(user);
                categoryRepository.save(categoryUpdate);

                DeleteCategoryResponse deleteCategoryResponse = new DeleteCategoryResponse();
                deleteCategoryResponse.setMessage("Delete Category successful");
                return new ResponseCommon<>(ResponseCode.SUCCESS, deleteCategoryResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Delete Category failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

//    @Override
//    public ResponseCommon<FindAllCategoryResponse> findAllCategory() {
//        try {
//            List<Category> listCategory = categoryRepository.findAllByIsDeleted(false);
//            // if the list is empty -> tell the user
//            if (listCategory.isEmpty()) {
//                log.debug("Get all Category failed: Category list is empty");
//                return new ResponseCommon<>(ResponseCode.CATEGORY_LIST_IS_EMPTY, null);
//            } else {
//                FindAllCategoryResponse response = new FindAllCategoryResponse("Get all success", listCategory);
//                log.debug("Get all Category successful");
//                return new ResponseCommon<>(ResponseCode.SUCCESS, response);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.debug("Get all Category failed: " + e.getMessage());
//            return new ResponseCommon<>(ResponseCode.FAIL, null);
//        }
//    }
}
