package com.example.android.bakingtime.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class StepContract {
    public static final String AUTHORITY = "com.example.android.bakingtime.provider.StepContract";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_STEPS = "steps";

    public static final long INVALID_STEP_ID = -1;

    public static final class StepEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STEPS).build();

        public static final String TABLE_NAME = "steps";
        public static final String COLUMN_STEP_ID = "id";
        public static final String COLUMN_RECIPE_ID = "recipeId";
        public static final String COLUMN_STEP_SHORT_DESCRIPTION = "shortDescription";
        public static final String COLUMN_STEP_DESCRIPTION = "description";
        public static final String COLUMN_STEP_VIDEO_URL = "videoUrl";
        public static final String COLUMN_STEP_THUMBNAIL_URL = "thumbnailUrl";
    }
}
