package com.example.demo.service;

import com.example.demo.web.payload.ReviewEditPayload;
import com.example.demo.web.payload.ReviewPayload;
import com.example.demo.web.result.MedicineResult;
import com.example.demo.web.result.PageResult;
import com.example.demo.web.result.ReviewResult;
import org.springframework.data.domain.PageRequest;

public interface ReviewService {
    Long save(ReviewPayload reviewPayload);

    ReviewResult findOneByReviewId(Long reviewId);

    Long editReview(Long reviewId, ReviewEditPayload reviewEditPayload);

    Long deleteByReviewId(Long reviewId);

    PageResult<ReviewResult> findPageByMedicineId(Long medicineId, PageRequest pageRequest);
}
