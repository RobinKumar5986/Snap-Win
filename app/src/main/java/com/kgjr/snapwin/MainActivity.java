package com.kgjr.snapwin;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;


public class MainActivity extends AppCompatActivity {

    private FrameLayout workspace;
    private EditText commandInput;
    private Button runButton;
    private int cellSize = 20;
    private int btnNo = 0;
    private int textInputNo = 0;
    private int labelNo = 0;


    private final HashMap<String, View> createdViews = new HashMap<>();
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        workspace = findViewById(R.id.workspace);
        commandInput = findViewById(R.id.commandInput);
        runButton = findViewById(R.id.runButton);

        runButton.setOnClickListener(v -> {
            String command = commandInput.getText().toString().trim();
            handleCommand(command);
        });
    }

    private void handleCommand(String command) {
        if (command.equalsIgnoreCase("c b")) {
            createNewButton(++btnNo);
        }
        else if (command.toLowerCase().startsWith("rz ")) {
            resizeView(command);
        }
        else if (command.toLowerCase().startsWith("mv ")) {
            moveView(command);
        }
        else if (command.toLowerCase().startsWith("cc ")) {
            changeColor(command);
        }
        else if (command.equalsIgnoreCase("c ti")) {
            createNewTextInput(++textInputNo);
        } else if (command.toLowerCase().startsWith("c l ")) {
            String labelText = command.substring(4).trim();
            if (!labelText.isEmpty()) {
                createNewLabel(++labelNo, labelText);
            } else {
                Toast.makeText(this, "Label text cannot be empty", Toast.LENGTH_SHORT).show();
            }
        } else if (command.toLowerCase().startsWith("dl")) {
            String[] parts = command.split(" ");
            if (parts.length == 2) {
                deleteView(parts[1].toUpperCase());
            } else {
                Toast.makeText(this, "Invalid delete command!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void createNewButton(int btnNo) {
        String btnId = "B" + btnNo;

        FrameLayout wrapper = new FrameLayout(this);
        FrameLayout.LayoutParams wrapperParams = new FrameLayout.LayoutParams(400, 200);
        wrapperParams.leftMargin = 0;
        wrapperParams.topMargin = 0;
        wrapper.setLayoutParams(wrapperParams);

        Button newButton = new Button(this);
        newButton.setText(btnId);
        newButton.setTextColor(Color.WHITE);
        newButton.setBackgroundColor(Color.DKGRAY);
        FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        newButton.setLayoutParams(buttonParams);


        Button resizeHandle = createResizeHandle();
        Button deleteButton = createDeleteButton(btnId);

        wrapper.addView(newButton);
        wrapper.addView(resizeHandle);
        wrapper.addView(deleteButton);

        setupInteractions(wrapper, newButton, resizeHandle, deleteButton);

        workspace.addView(wrapper);
        createdViews.put(btnId, wrapper);

        resizeHandle.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.VISIBLE);
        startHideTimer(resizeHandle);
        startHideTimer(deleteButton);
    }

    @SuppressLint("SetTextI18n")
    private void createNewLabel(int labelNo, String text) {
        String labelId = "L" + labelNo;

        FrameLayout wrapper = new FrameLayout(this);
        wrapper.setBackgroundColor(Color.TRANSPARENT);
        FrameLayout.LayoutParams wrapperParams = new FrameLayout.LayoutParams(400, 150);
        wrapperParams.leftMargin = 0;
        wrapperParams.topMargin = 0;
        wrapperParams.gravity = Gravity.TOP | Gravity.LEFT;
        wrapper.setLayoutParams(wrapperParams);

        TextView idLabel = new TextView(this);
        idLabel.setText(labelId);
        idLabel.setTextColor(Color.BLACK);
        idLabel.setTextSize(14);
        FrameLayout.LayoutParams idLabelParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        idLabelParams.gravity = Gravity.TOP | Gravity.RIGHT;
        idLabel.setLayoutParams(idLabelParams);
        idLabel.bringToFront();

        TextView label = getTextView(text, labelId);

        Button resizeHandle = createResizeHandle();
        Button deleteButton = createDeleteButton(labelId);

        wrapper.addView(label);
        wrapper.addView(resizeHandle);
        wrapper.addView(deleteButton);
        wrapper.addView(idLabel);

        setupInteractions(wrapper, wrapper, resizeHandle, deleteButton);

        workspace.addView(wrapper);
        createdViews.put(labelId, wrapper);

        resizeHandle.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.VISIBLE);
        startHideTimer(resizeHandle);
        startHideTimer(deleteButton);
    }

    @SuppressLint("SetTextI18n")
    private void createNewTextInput(int inputNo) {
        String inputId = "TI" + inputNo;

        FrameLayout wrapper = new FrameLayout(this);
        wrapper.setBackgroundColor(Color.WHITE);
        FrameLayout.LayoutParams wrapperParams = new FrameLayout.LayoutParams(400, 200);
        wrapperParams.leftMargin = 0;
        wrapperParams.topMargin = 0;
        wrapperParams.gravity = Gravity.TOP | Gravity.LEFT;
        wrapper.setLayoutParams(wrapperParams);

        TextView idLabel = new TextView(this);
        idLabel.setText(inputId);
        idLabel.setTextColor(Color.BLACK);
        idLabel.setTextSize(14);
        FrameLayout.LayoutParams idLabelParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        idLabelParams.gravity = Gravity.TOP | Gravity.RIGHT;
        idLabel.setLayoutParams(idLabelParams);
        idLabel.bringToFront();

        EditText editText = new EditText(this);
        editText.setHint("Enter Msg...");
        editText.setHintTextColor(Color.GRAY);
        editText.setTextColor(Color.BLACK);
        editText.setBackgroundColor(Color.LTGRAY);

        int padding = 40;
        FrameLayout.LayoutParams editParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        editParams.setMargins(padding, padding, padding, padding);
        editText.setLayoutParams(editParams);

        Button resizeHandle = createResizeHandle();
        Button deleteButton = createDeleteButton(inputId);

        wrapper.addView(editText);
        wrapper.addView(resizeHandle);
        wrapper.addView(deleteButton);
        wrapper.addView(idLabel);

        setupInteractions(wrapper, wrapper, resizeHandle, deleteButton);

        workspace.addView(wrapper);
        createdViews.put(inputId, wrapper);

        resizeHandle.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.VISIBLE);
        startHideTimer(resizeHandle);
        startHideTimer(deleteButton);
    }
    private Button createResizeHandle() {
        Button resizeHandle = new Button(this);
        resizeHandle.setText("↘");
        resizeHandle.setTextSize(18);
        resizeHandle.setPadding(0, 0, 0, 0);
        resizeHandle.setTextColor(Color.WHITE);
        resizeHandle.setBackgroundColor(Color.GREEN);
        FrameLayout.LayoutParams resizeParams = new FrameLayout.LayoutParams(60, 60, Gravity.BOTTOM | Gravity.RIGHT);
        resizeHandle.setLayoutParams(resizeParams);
        return resizeHandle;
    }

    private Button createDeleteButton(String viewId) {
        Button deleteButton = new Button(this);
        deleteButton.setText("x");
        deleteButton.setTextSize(18);
        deleteButton.setPadding(0, 0, 0, 0);
        deleteButton.setTextColor(Color.WHITE);
        deleteButton.setBackgroundColor(Color.RED);
        FrameLayout.LayoutParams deleteParams = new FrameLayout.LayoutParams(60, 60, Gravity.TOP | Gravity.LEFT);
        deleteButton.setLayoutParams(deleteParams);

        deleteButton.setOnClickListener(v -> deleteView(viewId));
        return deleteButton;
    }

    private void deleteView(String viewId) {
        View target = createdViews.get(viewId);
        if (target != null) {
            workspace.removeView(target);
            createdViews.remove(viewId);
            Toast.makeText(this, viewId + " deleted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Element " + viewId + " not found", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    private TextView getTextView(String text, String labelId) {
        TextView label = new TextView(this);
        label.setText(text);
        label.setTextColor(Color.BLACK);
        label.setTextSize(18);
        label.setBackgroundColor(Color.LTGRAY);
        label.setGravity(Gravity.CENTER);

        int padding = 20;
        FrameLayout.LayoutParams labelParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        labelParams.setMargins(padding, padding, padding, padding);
        label.setLayoutParams(labelParams);
        return label;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupInteractions(FrameLayout wrapper, View content, View resizeHandle, View deleteButton) {
        Runnable hideRunnable = () -> {
            resizeHandle.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        };

        content.setOnTouchListener(new View.OnTouchListener() {
            private float dX, dY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getPointerCount() > 1) return true;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        handler.removeCallbacks(hideRunnable);
                        resizeHandle.setVisibility(View.VISIBLE);
                        deleteButton.setVisibility(View.VISIBLE);
                        dX = event.getRawX() - wrapper.getLeft();
                        dY = event.getRawY() - wrapper.getTop();

                        if (content instanceof EditText) {
                            return false;
                        }
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        if (content instanceof EditText && content.hasFocus()) {
                            return false;
                        }

                        float newX = event.getRawX() - dX;
                        float newY = event.getRawY() - dY;

                        int snappedX = Math.round(newX / cellSize) * cellSize;
                        int snappedY = Math.round(newY / cellSize) * cellSize;

                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) wrapper.getLayoutParams();
                        params.leftMargin = snappedX;
                        params.topMargin = snappedY;
                        wrapper.setLayoutParams(params);
                        return true;

                    case MotionEvent.ACTION_UP:
                        handler.postDelayed(hideRunnable, 3000);
                        return content instanceof EditText ? false : true;
                }
                return false;
            }
        });

        resizeHandle.setOnTouchListener(new View.OnTouchListener() {
            private float initialRawX, initialRawY;
            private int initialWidth, initialHeight;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        handler.removeCallbacks(hideRunnable);
                        resizeHandle.setVisibility(View.VISIBLE);
                        deleteButton.setVisibility(View.VISIBLE);
                        initialRawX = event.getRawX();
                        initialRawY = event.getRawY();
                        initialWidth = wrapper.getWidth();
                        initialHeight = wrapper.getHeight();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        float deltaX = event.getRawX() - initialRawX;
                        float deltaY = event.getRawY() - initialRawY;

                        int newWidth = initialWidth + (int) deltaX;
                        int newHeight = initialHeight + (int) deltaY;

                        newWidth = Math.round(newWidth / (float) cellSize) * cellSize;
                        newHeight = Math.round(newHeight / (float) cellSize) * cellSize;

                        int maxWidth = workspace.getWidth() - wrapper.getLeft();
                        int maxHeight = workspace.getHeight() - wrapper.getTop();
                        newWidth = Math.max(cellSize, Math.min(newWidth, maxWidth));
                        newHeight = Math.max(cellSize, Math.min(newHeight, maxHeight));

                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) wrapper.getLayoutParams();
                        params.width = newWidth;
                        params.height = newHeight;
                        wrapper.setLayoutParams(params);
                        return true;

                    case MotionEvent.ACTION_UP:
                        handler.postDelayed(hideRunnable, 3000);
                        return true;
                }
                return false;
            }
        });
    }

    private void startHideTimer(View resizeHandle) {
        Runnable hideRunnable = () -> resizeHandle.setVisibility(View.GONE);
        handler.postDelayed(hideRunnable, 3000);
    }

    private void resizeView(String command) {
        String[] parts = command.split(" ");
        if (parts.length != 4) {
            Toast.makeText(this, "Invalid resize command!", Toast.LENGTH_SHORT).show();
            return;
        }

        String viewId = parts[1].toUpperCase();
        int targetWidth, targetHeight;

        try {
            targetWidth = Integer.parseInt(parts[2]);
            targetHeight = Integer.parseInt(parts[3]);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Width and Height must be numbers!", Toast.LENGTH_SHORT).show();
            return;
        }

        View target = createdViews.get(viewId);
        if (target == null) {
            Toast.makeText(this, "Element " + viewId + " not found", Toast.LENGTH_SHORT).show();
            return;
        }

        targetWidth = Math.round(targetWidth / (float) cellSize) * cellSize;
        targetHeight = Math.round(targetHeight / (float) cellSize) * cellSize;

        int maxWidth = workspace.getWidth() - target.getLeft();
        int maxHeight = workspace.getHeight() - target.getTop();
        targetWidth = Math.max(cellSize, Math.min(targetWidth, maxWidth));
        targetHeight = Math.max(cellSize, Math.min(targetHeight, maxHeight));

        final FrameLayout.LayoutParams finalParams = (FrameLayout.LayoutParams) target.getLayoutParams();
        final int finalStartWidth = finalParams.width;
        final int finalStartHeight = finalParams.height;
        final int finalTargetWidth = targetWidth;
        final int finalTargetHeight = targetHeight;

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(500);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(animation -> {
            float fraction = (float) animation.getAnimatedValue();
            finalParams.width = finalStartWidth + Math.round((finalTargetWidth - finalStartWidth) * fraction);
            finalParams.height = finalStartHeight + Math.round((finalTargetHeight - finalStartHeight) * fraction);
            target.setLayoutParams(finalParams);
        });
        animator.start();
    }

    private void moveView(String command) {
        String[] parts = command.split(" ");
        if (parts.length != 4) {
            Toast.makeText(this, "Invalid move command!", Toast.LENGTH_SHORT).show();
            return;
        }

        String viewId = parts[1].toUpperCase();
        int targetX, targetY;

        try {
            targetX = Integer.parseInt(parts[2]);
            targetY = Integer.parseInt(parts[3]);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Coordinates must be numbers!", Toast.LENGTH_SHORT).show();
            return;
        }

        View target = createdViews.get(viewId);
        if (target == null) {
            Toast.makeText(this, "Element " + viewId + " not found", Toast.LENGTH_SHORT).show();
            return;
        }

        int maxX = workspace.getWidth() - target.getWidth();
        int maxY = workspace.getHeight() - target.getHeight();
        if (targetX < 0 || targetY < 0 || targetX > maxX || targetY > maxY) {
            Toast.makeText(this, "Coordinates out of bounds!", Toast.LENGTH_SHORT).show();
            return;
        }

        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) target.getLayoutParams();
        final int startX = params.leftMargin;
        final int startY = params.topMargin;
        final int finalX = targetX;
        final int finalY = targetY;

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(500);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(animation -> {
            float fraction = (float) animation.getAnimatedValue();
            params.leftMargin = startX + Math.round((finalX - startX) * fraction);
            params.topMargin = startY + Math.round((finalY - startY) * fraction);
            target.setLayoutParams(params);
        });
        animator.start();

    }

    private void changeColor(String command) {
        String[] parts = command.split(" ");
        if (parts.length != 3) {
            Toast.makeText(this, "Invalid change color command!", Toast.LENGTH_SHORT).show();
            return;
        }

        String viewId = parts[1].toUpperCase();
        String colorName = parts[2].toLowerCase();

        View target = createdViews.get(viewId);
        if (target == null) {
            Toast.makeText(this, "Element " + viewId + " not found", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Integer> colorMap = new HashMap<>();
        colorMap.put("red", Color.RED);
        colorMap.put("green", Color.GREEN);
        colorMap.put("blue", Color.BLUE);
        colorMap.put("yellow", Color.YELLOW);
        colorMap.put("cyan", Color.CYAN);
        colorMap.put("magenta", Color.MAGENTA);
        colorMap.put("black", Color.BLACK);
        colorMap.put("white", Color.WHITE);
        colorMap.put("gray", Color.GRAY);
        colorMap.put("lightgray", Color.LTGRAY);
        colorMap.put("darkgray", Color.DKGRAY);

        Integer color = colorMap.get(colorName);
        if (color == null) {
            Toast.makeText(this, "Color \"" + colorName + "\" not recognized!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (target instanceof FrameLayout) {
            View content = ((FrameLayout) target).getChildAt(0);
            if (content != null) {
                content.setBackgroundColor(color);
            }
        } else {
            target.setBackgroundColor(color);
        }

    }
}