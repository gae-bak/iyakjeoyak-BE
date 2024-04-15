package com.example.demo.service.impl;

import com.example.demo.domain.entity.Review;
import com.example.demo.domain.entity.ReviewHashtag;
import com.example.demo.domain.repository.*;
import com.example.demo.service.ReviewService;
import com.example.demo.util.mapper.ReviewMapper;
import com.example.demo.web.payload.ReviewEditPayload;
import com.example.demo.web.payload.ReviewPayload;
import com.example.demo.web.result.PageResult;
import com.example.demo.web.result.ReviewResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final MedicineRepository medicineRepository;
    private final ReviewMapper reviewMapper;
    private final ReviewImageRepository imageRepository;
    private final ReviewHashtagRepository reviewHashtagRepository;
    private final HashtagRepository hashtagRepository;

    @Override
    @Transactional
    public Long save(ReviewPayload reviewPayload) {

        //TODO : 이미지 저장 로직 추가 필요

        Review review = reviewRepository.save(
                Review.builder()
                        .title(reviewPayload.getTitle())
                        .content(reviewPayload.getContent())
                        .star(reviewPayload.getStar())
                        .heartCount(0)
                        .medicine(medicineRepository.findById(reviewPayload.getMedicineId()).orElseThrow(() -> new NoSuchElementException("해당하는 영양제가 없습니다.")))
                        .build());

        reviewPayload.getTagList().forEach(
                ht -> reviewHashtagRepository.save(
                        ReviewHashtag.builder()
                                .review(review)
                                .hashtag(hashtagRepository.findById(ht).orElseThrow())
                                .build()));

        return review.getId();
    }

    @Override
    public ReviewResult findOneByReviewId(Long reviewId) {
        return reviewMapper.toDto(
                reviewRepository.findById(reviewId).orElseThrow(() -> new NoSuchElementException("리뷰가 존재하지 않습니다.")));
    }

    //이미지 수정은 따로 뺼 예정
    @Override
    @Transactional
    public Long editReview(Long reviewId, ReviewEditPayload reviewEditPayload) {

        //없어진 해쉬태그 삭제
        reviewHashtagRepository.findAllByReviewId(reviewId).forEach(ht -> {
            if (!reviewEditPayload.getTagList().contains(ht)) {
                reviewHashtagRepository.delete(ht);
            }
        });

        // 새로 추가된 hashtag 테이블 생성
        reviewEditPayload.getTagList().forEach(ht -> {
            if (reviewHashtagRepository.findByReviewIdAndHashtagId(reviewId, ht).orElse(null) == null) {
                reviewHashtagRepository.save(
                        ReviewHashtag.builder()
                                .review(reviewRepository.findById(reviewId).orElseThrow())
                                .hashtag(hashtagRepository.findById(ht).orElseThrow())
                                .build());
            }
        });

        Review review = reviewRepository.findById(reviewId).orElseThrow();
        return review.update(reviewEditPayload);
    }

    @Override
    public Long deleteByReviewId(Long reviewId) {
        reviewRepository.deleteById(reviewId);
        return reviewId;
    }

    @Override
    public PageResult<ReviewResult> findPageByMedicineId(Long medicineId, PageRequest pageRequest) {
        Page<ReviewResult> result = reviewRepository.findAllByMedicineId(medicineId, pageRequest).map(reviewMapper::toDto);
        return new PageResult<>(result);
    }
}
