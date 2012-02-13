        <link rel="stylesheet" href="/css/pdfjs/viewer.css"/>
        <script type="text/javascript" src="/js/pdfjs/compatibility.js"></script> <!-- PDFJSSCRIPT_REMOVE_FIREFOX_EXTENSION -->

        <!-- PDFJSSCRIPT_INCLUDE_BUILD -->
        <script type="text/javascript" src="/js/pdfjs/src/core.js"></script> <!-- PDFJSSCRIPT_REMOVE -->
        <script type="text/javascript" src="/js/pdfjs/src/util.js"></script>  <!-- PDFJSSCRIPT_REMOVE -->
        <script type="text/javascript" src="/js/pdfjs/src/canvas.js"></script>  <!-- PDFJSSCRIPT_REMOVE -->
        <script type="text/javascript" src="/js/pdfjs/src/obj.js"></script>  <!-- PDFJSSCRIPT_REMOVE -->
        <script type="text/javascript" src="/js/pdfjs/src/function.js"></script> <!-- PDFJSSCRIPT_REMOVE -->
        <script type="text/javascript" src="/js/pdfjs/src/charsets.js"></script>  <!-- PDFJSSCRIPT_REMOVE -->
        <script type="text/javascript" src="/js/pdfjs/src/cidmaps.js"></script>  <!-- PDFJSSCRIPT_REMOVE -->
        <script type="text/javascript" src="/js/pdfjs/src/colorspace.js"></script>  <!-- PDFJSSCRIPT_REMOVE -->
        <script type="text/javascript" src="/js/pdfjs/src/crypto.js"></script>  <!-- PDFJSSCRIPT_REMOVE -->
        <script type="text/javascript" src="/js/pdfjs/src/evaluator.js"></script>  <!-- PDFJSSCRIPT_REMOVE -->
        <script type="text/javascript" src="/js/pdfjs/src/fonts.js"></script>  <!-- PDFJSSCRIPT_REMOVE -->
        <script type="text/javascript" src="/js/pdfjs/src/glyphlist.js"></script>  <!-- PDFJSSCRIPT_REMOVE -->
        <script type="text/javascript" src="/js/pdfjs/src/image.js"></script>  <!-- PDFJSSCRIPT_REMOVE -->
        <script type="text/javascript" src="/js/pdfjs/src/metrics.js"></script>  <!-- PDFJSSCRIPT_REMOVE -->
        <script type="text/javascript" src="/js/pdfjs/src/parser.js"></script>  <!-- PDFJSSCRIPT_REMOVE -->
        <script type="text/javascript" src="/js/pdfjs/src/pattern.js"></script>  <!-- PDFJSSCRIPT_REMOVE -->
        <script type="text/javascript" src="/js/pdfjs/src/stream.js"></script>  <!-- PDFJSSCRIPT_REMOVE -->
        <script type="text/javascript" src="/js/pdfjs/src/worker.js"></script>  <!-- PDFJSSCRIPT_REMOVE -->
        <script type="text/javascript" src="/js/pdfjs/external/jpgjs/jpg.js"></script>  <!-- PDFJSSCRIPT_REMOVE -->
        <script type="text/javascript" src="/js/pdfjs/src/jpx.js"></script>  <!-- PDFJSSCRIPT_REMOVE -->
        <script type="text/javascript">PDFJS.workerSrc = '/js/pdfjs/src/worker_loader.js';</script> <!-- PDFJSSCRIPT_REMOVE -->
        <script type="text/javascript" src="/js/pdfjs/viewer.js"></script>

    <div id="controls">
      <button id="previous" onclick="PDFView.page--;" oncontextmenu="return false;">
        <img src="/images/pdfjs/go-up.svg" align="top" height="16"/>
        Previous
      </button>

      <button id="next" onclick="PDFView.page++;" oncontextmenu="return false;">
        <img src="/images/pdfjs/go-down.svg" align="top" height="16"/>
        Next
      </button>

      <div class="separator"></div>

      <input type="number" id="pageNumber" onchange="PDFView.page = this.value;" value="1" size="4" min="1" />

      <span>/</span>
      <span id="numPages">--</span>

      <div class="separator"></div>

      <button id="zoomOut" title="Zoom Out" onclick="PDFView.zoomOut();" oncontextmenu="return false;">
        <img src="/images/pdfjs/zoom-out.svg" align="top" height="16"/>
      </button>
      <button id="zoomIn" title="Zoom In" onclick="PDFView.zoomIn();" oncontextmenu="return false;">
        <img src="/images/pdfjs/zoom-in.svg" align="top" height="16"/>
      </button>

      <div class="separator"></div>

      <select id="scaleSelect" onchange="PDFView.parseScale(this.value);" oncontextmenu="return false;">
        <option id="customScaleOption" value="custom"></option>
        <option value="0.5">50%</option>
        <option value="0.75">75%</option>
        <option value="1">100%</option>
        <option value="1.25">125%</option>
        <option value="1.5">150%</option>
        <option value="2">200%</option>
        <option id="pageWidthOption" value="page-width">Page Width</option>
        <option id="pageFitOption" value="page-fit">Page Fit</option>
        <option id="pageAutoOption" value="auto" selected="selected">Auto</option>
      </select>

      <div class="separator"></div>

      <button id="print" onclick="window.print();" oncontextmenu="return false;">
        <img src="/images/pdfjs/document-print.svg" align="top" height="16"/>
        Print
      </button>

      <button id="download" title="Download" onclick="PDFView.download();" oncontextmenu="return false;">
        <img src="/images/pdfjs/download.svg" align="top" height="16"/>
        Download
      </button>

      <!--<div class="separator"></div>-->

      <input id="fileInput" style="display:none;" type="file" oncontextmenu="return false;"/>

      <div id="fileInputSeperator" style="display:none;" class="separator"></div>

      <a href="#" id="viewBookmark" style="display:none;"  title="Bookmark (or copy) current location">
        <img src="/images/pdfjs/bookmark.svg" alt="Bookmark" align="top" height="16"/>
      </a>

      <span id="info">--</span>
    </div>
    <div id="errorWrapper" hidden='true'>
      <div id="errorMessageLeft">
        <span id="errorMessage"></span>
        <button id="errorShowMore" onclick="" oncontextmenu="return false;">
          More Information
        </button>
        <button id="errorShowLess" onclick="" oncontextmenu="return false;" hidden='true'>
          Less Information
        </button>
      </div>
      <div id="errorMessageRight">
        <button id="errorClose" oncontextmenu="return false;">
          Close
        </button>
      </div>
      <div class="clearBoth"></div>
      <textarea id="errorMoreInfo" hidden='true' readonly="readonly"></textarea>
    </div>

    <div id="sidebar" style="display:none;">
      <div id="sidebarBox">
        <div id="sidebarScrollView">
          <div id="sidebarView"></div>
        </div>
        <div id="outlineScrollView" hidden='true'>
          <div id="outlineView"></div>
        </div>
        <div id="sidebarControls">
          <button id="thumbsSwitch" title="Show Thumbnails" onclick="PDFView.switchSidebarView('thumbs')" data-selected>
            <img src="/images/pdfjs/nav-thumbs.svg" align="top" height="16" alt="Thumbs" />
          </button>
          <button id="outlineSwitch" title="Show Document Outline" onclick="PDFView.switchSidebarView('outline')" disabled>
            <img src="/images/pdfjs/nav-outline.svg" align="top" height="16" alt="Document Outline" />
          </button>
        </div>
     </div>
    </div>

    <div id="loading">Loading... 0%</div>
    <div id="viewer"></div>


