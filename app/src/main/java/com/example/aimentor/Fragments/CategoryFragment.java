package com.example.aimentor.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;

import com.example.aimentor.activities.LessonDetailActivity;
import com.example.aimentor.models.LessonModel;
import com.example.aimentor.network.LessonApiClient;

import com.example.aimentor.R;
import com.example.aimentor.activities.MathlsActivity;
import com.example.aimentor.activities.ProgrammingActivity;
import com.example.aimentor.models.CourseModel;
import com.example.aimentor.network.CourseApiClient;
import com.example.aimentor.repository.CourseRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryFragment extends Fragment {

//    private ImageView imgDropDownProg, imgDropDownMath;
//    private LinearLayout layoutDropDownProg, layoutDropDownMath;
//    private Button btnProgLs1, btnMathLs1, btnMathLs2;
    private LinearLayout layoutCourses;
    private TextView tvCourseStatus;
    private final ExecutorService networkExecutor = Executors.newSingleThreadExecutor();

    public CategoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

//        // Programming Category
//        imgDropDownProg = view.findViewById(R.id.imgDropDownProg);
//        layoutDropDownProg = view.findViewById(R.id.layoutDropDownProg);
//        btnProgLs1 = view.findViewById(R.id.btnProgLs1);
//
//        imgDropDownProg.setOnClickListener(v -> toggleLayout(layoutDropDownProg, imgDropDownProg));
//
//        btnProgLs1.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), ProgrammingActivity.class);
//            startActivity(intent);
//        });
//
//        // Math Category
//        imgDropDownMath = view.findViewById(R.id.imgDropDownMath);
//        layoutDropDownMath = view.findViewById(R.id.layoutDropDownMath);
//        btnMathLs1 = view.findViewById(R.id.btnMathLs1);
//        btnMathLs2 = view.findViewById(R.id.btnMathLs2);
//
//        imgDropDownMath.setOnClickListener(v -> toggleLayout(layoutDropDownMath, imgDropDownMath));
//
//        btnMathLs1.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), MathlsActivity.class);
//            startActivity(intent);
//        });
//
//        btnMathLs2.setOnClickListener(v -> {
//            // Placeholder for Math Lesson 2
//        });

        layoutCourses = view.findViewById(R.id.layoutServerCourses);
//        tvCourseStatus = view.findViewById(R.id.tvCourseStatus);
        loadCoursesFromApi();

        return view;
    }

    private void loadCoursesFromApi() {
        networkExecutor.execute(() -> {
            CourseRepository repository = new CourseRepository(requireContext().getApplicationContext());
            try {
                List<CourseModel> courses = new CourseApiClient().fetchCourses();
                repository.saveCoursesFromServer(courses);
                showCourses(courses, "Đã cập nhật " + courses.size() + " khóa học từ server.");
            } catch (Exception e) {
                List<CourseModel> cachedCourses = repository.getCachedCourses();
                String message = cachedCourses.isEmpty()
                        ? "Không kết nối được API. Hãy chạy AIMentorApi trên máy tính."
                        : "Không kết nối được API. Đang hiển thị dữ liệu đã lưu offline.";
                showCourses(cachedCourses, message);
            }
        });
    }

    private void showCourses(
            List<CourseModel> courses,
            String status
    ) {
        if (!isAdded()) {
            return;
        }

        requireActivity().runOnUiThread(() -> {
            if (!isAdded()) {
                return;
            }

            layoutCourses.removeAllViews();

            // Trường hợp database không có môn học
            if (courses == null || courses.isEmpty()) {
                TextView emptyView =
                        new TextView(requireContext());

                emptyView.setText(
                        status == null || status.isEmpty()
                                ? "No courses are available."
                                : status
                );

                emptyView.setTextSize(16);
                emptyView.setTextColor(0xFF697386);
                emptyView.setGravity(
                        android.view.Gravity.CENTER
                );
                emptyView.setPadding(20, 80, 20, 80);

                layoutCourses.addView(emptyView);
                return;
            }

            LayoutInflater inflater =
                    LayoutInflater.from(requireContext());

            for (CourseModel course : courses) {
                // Tạo card từ item_course.xml
                View courseView = inflater.inflate(
                        R.layout.item_course,
                        layoutCourses,
                        false
                );

                TextView tvCourseTitle =
                        courseView.findViewById(
                                R.id.tvCourseTitle
                        );

                TextView tvCourseDescription =
                        courseView.findViewById(
                                R.id.tvCourseDescription
                        );

                TextView tvCourseStatus =
                        courseView.findViewById(
                                R.id.tvCourseStatus
                        );

                LinearLayout layoutCourseHeader =
                        courseView.findViewById(
                                R.id.layoutCourseHeader
                        );

                LinearLayout layoutLessons =
                        courseView.findViewById(
                                R.id.layoutLessons
                        );

                ImageView ivExpandCourse =
                        courseView.findViewById(
                                R.id.ivExpandCourse
                        );

                // Đưa dữ liệu database lên giao diện
                tvCourseTitle.setText(course.getTitle());

                String description =
                        course.getDescription();

                if (description == null ||
                        description.trim().isEmpty()) {

                    tvCourseDescription.setVisibility(
                            View.GONE
                    );

                } else {
                    tvCourseDescription.setVisibility(
                            View.VISIBLE
                    );

                    tvCourseDescription.setText(
                            description
                    );
                }

                if (course.isPublished()) {
                    tvCourseStatus.setText("Available");
                    tvCourseStatus.setTextColor(0xFF3346D3);
                } else {
                    tvCourseStatus.setText("Unavailable");
                    tvCourseStatus.setTextColor(0xFF697386);
                }

                // Mở hoặc đóng danh sách bài học
                layoutCourseHeader.setOnClickListener(v -> {
                    boolean shouldOpen =
                            layoutLessons.getVisibility()
                                    == View.GONE;

                    if (shouldOpen &&
                            layoutLessons.getChildCount() == 0) {

                        loadLessons(
                                course.getServerId(),
                                course.getTitle(),
                                layoutLessons
                        );
                    }

                    layoutLessons.setVisibility(
                            shouldOpen
                                    ? View.VISIBLE
                                    : View.GONE
                    );

                    ivExpandCourse.animate()
                            .rotation(shouldOpen ? 180f : 0f)
                            .setDuration(200)
                            .start();
                });

                layoutCourses.addView(courseView);
            }
        });
    }

    private void loadLessons(int courseId, String courseTitle, LinearLayout lessonLayout) {

        networkExecutor.execute(() -> {

            try {

                List<LessonModel> lessons =
                        new LessonApiClient()
                                .fetchLessons(courseId);

                requireActivity().runOnUiThread(() -> {

                    for (LessonModel lesson : lessons) {

                        Button btn =
                                new Button(requireContext());

                        btn.setText(lesson.getTitle());

                        btn.setAllCaps(false);

                        btn.setOnClickListener(v -> {
                            Intent intent = new Intent(requireContext(), LessonDetailActivity.class);
                            intent.putExtra("COURSE_TITLE", courseTitle);
                            intent.putExtra("LESSON_TITLE", lesson.getTitle());
                            intent.putExtra("LESSON_CONTENT", lesson.getContent());
                            intent.putExtra("LESSON_ID", lesson.getId());
                            startActivity(intent);
                        });

                        lessonLayout.addView(btn);

                    }

                });

            } catch (Exception e) {

                requireActivity().runOnUiThread(() ->

                        Toast.makeText(
                                requireContext(),
                                "Không tải được Lesson",
                                Toast.LENGTH_SHORT
                        ).show());

            }

        });

    }

    private void toggleLayout(LinearLayout layout, ImageView icon) {
        if (layout.getVisibility() == View.GONE) {
            layout.setVisibility(View.VISIBLE);
            icon.setImageResource(android.R.drawable.arrow_up_float);
        } else {
            layout.setVisibility(View.GONE);
            icon.setImageResource(android.R.drawable.arrow_down_float);
        }
    }

    @Override
    public void onDestroyView() {
        networkExecutor.shutdownNow();
        super.onDestroyView();
    }
}
