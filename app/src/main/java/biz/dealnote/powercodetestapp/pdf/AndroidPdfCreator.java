package biz.dealnote.powercodetestapp.pdf;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import biz.dealnote.powercodetestapp.utils.ResourceUtils;
import io.reactivex.Completable;
import io.reactivex.functions.Action;

/**
 * Created by r.kolbasa on 29.12.2017.
 * PowercodeTestApp
 */
public class AndroidPdfCreator implements IPdfCreator {

    private final Context context;

    public AndroidPdfCreator(Context context) {
        this.context = context.getApplicationContext();
    }

    private static final int DEF_MARGIN = 200;

    @Override
    public Completable create(@NonNull File file, @NonNull List<Page> pages) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                FileOutputStream os = null;

                try {
                    os = new FileOutputStream(file);
                    PrintAttributes attributes = new PrintAttributes.Builder()
                            .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                            .setMinMargins(new PrintAttributes.Margins(DEF_MARGIN, DEF_MARGIN, DEF_MARGIN, DEF_MARGIN))
                            .build();

                    // open a new document
                    PrintedPdfDocument document = new PrintedPdfDocument(context, attributes);

                    for (int i = 0; i < pages.size(); i++) {
                        Page p = pages.get(i);

                        // start a page
                        PdfDocument.Page page = document.startPage(0);

                        drawPage(page, p);

                        // finish the page
                        document.finishPage(page);
                    }

                    // write the document content
                    document.writeTo(os);

                    //close the document
                    document.close();

                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                } finally {
                    if (os != null) {
                        os.close();
                    }
                }
            }
        });
    }

    private void drawPage(PdfDocument.Page page, Page p) throws FileNotFoundException {
        // draw something on the page
        Canvas canvas = page.getCanvas();

        int bitmapH;

        InputStream is = null;
        Bitmap origBitmap = null;
        Bitmap scaledBitmap = null;

        try {
            is = context.getContentResolver().openInputStream(p.getImage());
            origBitmap = BitmapFactory.decodeStream(is);
            scaledBitmap = scaleDown(origBitmap, canvas.getWidth(), true);

            bitmapH = scaledBitmap.getHeight();
            canvas.drawBitmap(scaledBitmap, 0, 0, new Paint());
        } finally {
            ResourceUtils.safelyClose(is);
            ResourceUtils.safelyRecycle(origBitmap);
            ResourceUtils.safelyRecycle(scaledBitmap);
        }

        String textOnCanvas = p.getComment();

        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(24);

        //Static layout which will be drawn on canvas
        //textOnCanvas - text which will be drawn
        //text paint - paint object
        //bounds.width - width of the layout
        //Layout.Alignment.ALIGN_CENTER - layout alignment
        //1 - text spacing multiply
        //1 - text spacing add
        //true - include padding
        StaticLayout sl = new StaticLayout(textOnCanvas, textPaint, canvas.getWidth(),
                Layout.Alignment.ALIGN_NORMAL, 1, 1, true);

        canvas.save();

        //calculate X and Y coordinates - In this case we want to draw the text in the
        //center of canvas so we calculate
        //text height and number of lines to move Y coordinate to center.
        float textHeight = getTextHeight(textOnCanvas, textPaint);
        int numberOfTextLines = sl.getLineCount();

        //float textYCoordinate = bounds.exactCenterY() - ((numberOfTextLines * textHeight) / 2);

        float textYCoordinate = bitmapH;

        //text will be drawn from left
        float textXCoordinate = 0;

        canvas.translate(textXCoordinate, textYCoordinate);

        //draws static layout on canvas
        sl.draw(canvas);
        canvas.restore();
    }

    private static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        if (realImage.getHeight() < maxImageSize && realImage.getWidth() < maxImageSize) {
            return realImage;
        }

        float ratio = Math.min(maxImageSize / realImage.getWidth(), maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());
        return Bitmap.createScaledBitmap(realImage, width, height, filter);
    }

    /**
     * @return text height
     */
    private static float getTextHeight(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }
}