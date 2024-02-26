package in.vakrangee.franchisee.drawingtool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.byox.drawview.enums.BackgroundScale;
import com.byox.drawview.enums.BackgroundType;
import com.byox.drawview.enums.DrawingCapture;
import com.byox.drawview.enums.DrawingMode;
import com.byox.drawview.enums.DrawingTool;
import com.byox.drawview.views.DrawView;

import java.io.ByteArrayOutputStream;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class DrawingToolActivity extends AppCompatActivity {

    private static final String TAG = "DrawingToolActivity";
    private Context context;
    private DrawView drawView;
    private LinearLayout layoutDrawMode, layoutDrawingTool;
    private ImageView txtRedo, txtDelete, txtUndo;
    private String[] CHOICES_ARRAY = {"PEN", "LINE", "ARROW", "RECTANGLE", "CIRCLE", "ELLIPSE"};
    private DeprecateHandler deprecateHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_sketch_view);

        //Initialize data
        this.context = this;
        deprecateHandler = new DeprecateHandler(context);

        //Widgets
        drawView = (DrawView) findViewById(R.id.draw_view);
        /*drawView.setPaintStyle(Paint.Style.FILL);
        drawView.setFontSize(24F);*/

        layoutDrawMode = findViewById(R.id.layoutDrawMode);
        layoutDrawingTool = findViewById(R.id.layoutDrawingTool);
        txtRedo = findViewById(R.id.layoutRedo);
        txtDelete = findViewById(R.id.layoutDelete);
        txtUndo = findViewById(R.id.layoutUndo);

        //Draw Mode
        layoutDrawMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDrawMode();
            }
        });

        //Drawing Tool
        layoutDrawingTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDrawTool();
            }
        });

        //Redo
        txtRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawView.canRedo()) {
                    drawView.redo();
                    canUndoRedo();
                }
            }
        });

        //Undo
        txtUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawView.canUndo()) {
                    drawView.undo();
                    canUndoRedo();
                }
            }
        });

        //Delete
        txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearDraw();
            }
        });

        //Draw view
        drawView.setOnDrawViewListener(new DrawView.OnDrawViewListener() {
            @Override
            public void onStartDrawing() {
                canUndoRedo();
            }

            @Override
            public void onEndDrawing() {
                canUndoRedo();
            }

            @Override
            public void onClearDrawing() {
                canUndoRedo();
            }

            @Override
            public void onRequestText() {
                RequestTextDialog requestTextDialog = RequestTextDialog.newInstance("");
                requestTextDialog.setOnRequestTextListener(new RequestTextDialog.OnRequestTextListener() {
                    @Override
                    public void onRequestTextConfirmed(String requestedText) {
                        if (!drawView.isDrawViewEmpty())
                            drawView.refreshLastText(requestedText);
                    }

                    @Override
                    public void onRequestTextCancelled() {
                        drawView.cancelTextRequest();
                    }
                });
                requestTextDialog.show(getSupportFragmentManager(), "requestText");
            }

            @Override
            public void onAllMovesPainted() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        canUndoRedo();
                    }
                }, 300);
            }
        });
    }

    private void canUndoRedo() {
        if (!drawView.canUndo()) {
            txtUndo.setEnabled(false);
            txtUndo.setImageDrawable(deprecateHandler.getDrawable(R.drawable.ic_undo_disabled_grey_32dp));
        } else {
            txtUndo.setEnabled(true);
            txtUndo.setImageDrawable(deprecateHandler.getDrawable(R.drawable.ic_undo_white_24dp));
        }
        if (!drawView.canRedo()) {
            txtRedo.setEnabled(false);
            txtRedo.setImageDrawable(deprecateHandler.getDrawable(R.drawable.ic_redo_disabled_grey_32dp));
        } else {
            txtRedo.setEnabled(true);
            txtRedo.setImageDrawable(deprecateHandler.getDrawable(R.drawable.ic_redo_white_24dp));
        }
    }

    public void clearDraw() {
        drawView.restartDrawing();
    }

    private void changeDrawMode() {
        SelectChoiceDialog selectChoiceDialog = SelectChoiceDialog.newInstance("Select a draw mode", "DRAW", "TEXT", "ERASER");
        selectChoiceDialog.setOnChoiceDialogListener(new SelectChoiceDialog.OnChoiceDialogListener() {
            @Override
            public void onChoiceSelected(int position) {
                drawView.setDrawingMode(DrawingMode.values()[position]);
            }
        });
        selectChoiceDialog.show(getSupportFragmentManager(), "choiceDialog");
    }

    private void changeDrawTool() {
        SelectChoiceDialog selectChoiceDialog = SelectChoiceDialog.newInstance("Select a draw tool", "PEN", "LINE", "ARROW", "RECTANGLE", "CIRCLE", "ELLIPSE");
        selectChoiceDialog.setOnChoiceDialogListener(new SelectChoiceDialog.OnChoiceDialogListener() {
            @Override
            public void onChoiceSelected(int position) {
                //drawView.setDrawingTool(DrawingTool.valueOf(CHOICES_ARRAY[position]));
                drawView.setDrawingTool(DrawingTool.values()[position]);
            }
        });
        selectChoiceDialog.show(getSupportFragmentManager(), "choiceDialog");
    }

    public void setBitmap(Bitmap bitmap) {
        Canvas myCanvas = new Canvas();
        myCanvas.drawBitmap(bitmap, 0, 0, null);
        clearDraw();
        drawView.setBackgroundImage(bitmap, BackgroundType.BITMAP, BackgroundScale.FIT_XY);
    }

    public void getBitmap() {
        Object[] createCaptureResponse = drawView.createCapture(DrawingCapture.BITMAP);
        Bitmap imgBitmap = (Bitmap) createCaptureResponse[0];
        String data = convertBitmapToBase64(imgBitmap);
        Log.d("", "Testing: Image Base64 : " + data);
    }

    public String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }
}
