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

    private void showCourses(List<CourseModel> courses, String status) {
//        if (!isAdded()) return;
//        requireActivity().runOnUiThread(() -> {
//            tvCourseStatus.setText(status);
//            layoutServerCourses.removeAllViews();
//
//            for (CourseModel course : courses) {
//                LinearLayout item = new LinearLayout(requireContext());
//                item.setOrientation(LinearLayout.VERTICAL);
//                item.setPadding(24, 20, 24, 20);
//                LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                itemParams.setMargins(8, 8, 8, 8);
//                item.setLayoutParams(itemParams);
//                item.setBackgroundColor(0xFFE3F2FD);
//
//                TextView title = new TextView(requireContext());
//                title.setText(course.getTitle());
//                title.setTextSize(18);
//                title.setTextColor(0xFF000000);
//                title.setTypeface(null, android.graphics.Typeface.BOLD);
//                item.addView(title);
//
//                if (course.getDescription() != null && !course.getDescription().isEmpty()) {
//                    TextView description = new TextView(requireContext());
//                    description.setText(course.getDescription());
//                    description.setTextColor(0xFF333333);
//                    item.addView(description);
//                }
//
//                item.setOnClickListener(v -> Toast.makeText(requireContext(),
//                        "Đã chọn: " + course.getTitle(), Toast.LENGTH_SHORT).show());
//                layoutServerCourses.addView(item);
//            }
//        });


        if (!isAdded()) return;

        requireActivity().runOnUiThread(() -> {

            layoutCourses.removeAllViews();

            for (CourseModel course : courses) {

                CardView card = new CardView(requireContext());
                card.setRadius(20);
                card.setCardElevation(8);

                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 20, 0, 20);
                card.setLayoutParams(params);

                LinearLayout root = new LinearLayout(requireContext());
                root.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(requireContext());
                title.setText(course.getTitle());
                title.setTextSize(20);
                title.setPadding(40, 40, 40, 40);
                title.setTypeface(null, android.graphics.Typeface.BOLD);

                LinearLayout lessonLayout = new LinearLayout(requireContext());
                lessonLayout.setOrientation(LinearLayout.VERTICAL);
                lessonLayout.setVisibility(View.GONE);

                root.setOnClickListener(v -> {

                    if (lessonLayout.getChildCount() == 0) {
                        loadLessons(course.getServerId(), course.getTitle(), lessonLayout);
                    }

                    if (lessonLayout.getVisibility() == View.GONE) {
                        lessonLayout.setVisibility(View.VISIBLE);
                    } else {
                        lessonLayout.setVisibility(View.GONE);
                    }
                });

                root.addView(title);
                root.addView(lessonLayout);

                card.addView(root);

                layoutCourses.addView(card);
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
