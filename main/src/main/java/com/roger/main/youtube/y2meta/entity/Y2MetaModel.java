package com.roger.main.youtube.y2meta.entity;

import java.io.Serializable;


/**
 * "links": {
 *         "mp4": {
 *             "137": {
 *                 "size": "94.2 MB",
 *                 "f": "mp4",
 *                 "q": "1080p",
 *                 "q_text": "1080p (.mp4) <span class=\"label label-primary\"><small>HD<\/small><\/span>",
 *                 "k": "joMTXIOxwJb0bpGh6qGEqMb8Al7j4vZwhNgqkwxyUfMcut53mb30bZgfdulZ0YeyRcIFpmk="
 *             },
 *             "136": {
 *                 "size": "52.8 MB",
 *                 "f": "mp4",
 *                 "q": "720p",
 *                 "q_text": "720p (.mp4) <span class=\"label label-primary\"><small>m-HD<\/small><\/span>",
 *                 "k": "joMTXIOxwJb0bpGh6qGEqMb8Al7j4vZwhNgqkwx0U\/tcut53mb30bZgfdulZ0YeyRcIFpmg="
 *             },
 *             "18": {
 *                 "size": "16.9 MB",
 *                 "f": "mp4",
 *                 "q": "360p",
 *                 "q_text": "360p (.mp4)",
 *                 "k": "joMTXIOxwJb0bpGh6qGEqMb8Al7j4vZwhNgqkwxwV\/tQ9dVhjrDucpkXYPETwJr2DY8M"
 *             },
 *             "160": {
 *                 "size": "9 MB",
 *                 "f": "mp4",
 *                 "q": "144p",
 *                 "q_text": "144p (.mp4)",
 *                 "k": "joMTXIOxwJb0bpGh6qGEqMb8Al7j4vZwhNgqkwxyVf9cut53mb30bZgfdulZ0YeyRcIFo24="
 *             },
 *             "3gp@144p": {
 *                 "size": "MB",
 *                 "f": "3gp",
 *                 "q": "144p",
 *                 "q_text": "144p (.3gp)",
 *                 "k": "joMTXIOxwJb0bpGh6qGEqMb8Al7j4vZwhIY91wxyVf9cut53mb30bZgfdulZ0YeyRcIH8i74IITfIA=="
 *             },
 *             "auto": {
 *                 "size": "",
 *                 "f": "mp4",
 *                 "q": "auto",
 *                 "selected": "selected",
 *                 "q_text": "MP4 auto quality",
 *                 "k": "joMTXIOxwJb0bpGh6qGEqMb8Al7j4vZwhNgqkwwiFL9Dut53mb30bZgfdulZ"
 *             }
 *         },
 *         "mp3": {
 *             "140": {
 *                 "size": "6 MB",
 *                 "f": "m4a",
 *                 "q": ".m4a",
 *                 "q_text": ".m4a (128kbps)",
 *                 "k": "joMTXIOxwJb0bpGh6qGEqMb8Al7j4vZwhNhuxgxyU\/NQ9dVhjrDucpkXYPETwJrxDY8ApQ=="
 *             },
 *             "mp3128": {
 *                 "size": "6 MB",
 *                 "f": "mp3",
 *                 "q": "128kbps",
 *                 "q_text": "MP3 - 128kbps",
 *                 "k": "joMTXIOxwJb0bpGh6qGEqMb8Al7j4vZwhNgqlAxyU\/NQ9dVhjrDucpkXYPE="
 *             }
 *         },
 *         "other": {
 *             "249": {
 *                 "size": "2.3 MB",
 *                 "f": "webm",
 *                 "q": "Audio",
 *                 "q_text": "Audio .webm (48kbps)",
 *                 "k": "joMTXIOxwJb0bpGh6qGEqMb8Al7j4vZwhMI\/xR0\/VfNQ9dVhjrDucpkXYPETwp6qFMxIp2qB"
 *             },
 *             "250": {
 *                 "size": "2.7 MB",
 *                 "f": "webm",
 *                 "q": "Audio",
 *                 "q_text": "Audio .webm (64kbps)",
 *                 "k": "joMTXIOxwJb0bpGh6qGEqMb8Al7j4vZwhMI\/xR0\/V\/9Q9dVhjrDucpkXYPETwp6qFMxIp2uI"
 *             },
 *             "251": {
 *                 "size": "4.9 MB",
 *                 "f": "webm",
 *                 "q": "Audio",
 *                 "q_text": "Audio .webm (160kbps)",
 *                 "k": "joMTXIOxwJb0bpGh6qGEqMb8Al7j4vZwhMI\/xR0\/UP0cut53mb30bZgfdulZ0YW2GdtG6WyNIA=="
 *             },
 *             "160": {
 *                 "size": "9 MB",
 *                 "f": "mp4",
 *                 "q": "Video",
 *                 "q_text": "Video only 144p (MP4)",
 *                 "k": "joMTXIOxwJb0bpGh6qGEqMb8Al7j4vZwhNgqkwxyVf9cut53mb30bZgfdulZ0YW2GdtG6W+OIQ=="
 *             },
 *             "278": {
 *                 "size": "3.1 MB",
 *                 "f": "webm",
 *                 "q": "Video",
 *                 "q_text": "Video only 144p (WEBM)",
 *                 "k": "joMTXIOxwJb0bpGh6qGEqMb8Al7j4vZwhMI\/xR0\/UP8YtpF8j6r5d4cefv9Bm5atBdZR5yKKJog="
 *             },
 *             "242": {
 *                 "size": "5.6 MB",
 *                 "f": "webm",
 *                 "q": "Video",
 *                 "q_text": "Video only 240P  (WEBM)",
 *                 "k": "joMTXIOxwJb0bpGh6qGEqMb8Al7j4vZwhMI\/xR0\/U\/8ctpF8j6r5d4cefv9Bm5atBdZR5yKKJYI="
 *             },
 *             "134": {
 *                 "size": "18.5 MB",
 *                 "f": "mp4",
 *                 "q": "Video",
 *                 "q_text": "Video only 360P (MP4)",
 *                 "k": "joMTXIOxwJb0bpGh6qGEqMb8Al7j4vZwhNgqkwxwV\/tcut53mb30bZgfdulZ0YW2GdtG6W+LJQ=="
 *             },
 *             "243": {
 *                 "size": "9.7 MB",
 *                 "f": "webm",
 *                 "q": "Video",
 *                 "q_text": "Video only 360P  (WEBM)",
 *                 "k": "joMTXIOxwJb0bpGh6qGEqMb8Al7j4vZwhMI\/xR0\/Uv0ctpF8j6r5d4cefv9Bm5atBdZR5yKKJYM="
 *             },
 *             "244": {
 *                 "size": "16 MB",
 *                 "f": "webm",
 *                 "q": "Video",
 *                 "q_text": "Video only 480P  (WEBM)",
 *                 "k": "joMTXIOxwJb0bpGh6qGEqMb8Al7j4vZwhMI\/xR0\/VfMctpF8j6r5d4cefv9Bm5atBdZR5yKKJYQ="
 *             },
 *             "136": {
 *                 "size": "52.8 MB",
 *                 "f": "mp4",
 *                 "q": "Video",
 *                 "q_text": "Video only 480P (MP4)",
 *                 "k": "joMTXIOxwJb0bpGh6qGEqMb8Al7j4vZwhNgqkwx0U\/tcut53mb30bZgfdulZ0YW2GdtG6W+LJw=="
 *             },
 *             "137": {
 *                 "size": "94.2 MB",
 *                 "f": "mp4",
 *                 "q": "Video",
 *                 "q_text": "Video only (MP4)<span class=\"label label-primary\"><small>1K<\/small><\/span>",
 *                 "k": "joMTXIOxwJb0bpGh6qGEqMb8Al7j4vZwhNgqkwxyUfMcut53mb30bZgfdulZ0YW2GdtG6W+LJg=="
 *             },
 *             "248": {
 *                 "size": "48.3 MB",
 *                 "f": "webm",
 *                 "q": "Video",
 *                 "q_text": "Video only (WEBM)<span class=\"label label-primary\"><small>1K<\/small><\/span>",
 *                 "k": "joMTXIOxwJb0bpGh6qGEqMb8Al7j4vZwhMI\/xR0\/UPsU9pF8j6r5d4cefv9Bm5atBdZR5yKKJYg="
 *             }
 *         }
 *     },
 */
public class Y2MetaModel implements Serializable {
    String size;
    String f;
    String q;
    String q_text;
    String k;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getF() {
        return f;
    }

    public void setF(String f) {
        this.f = f;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getQ_text() {
        return q_text;
    }

    public void setQ_text(String q_text) {
        this.q_text = q_text;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }
}
