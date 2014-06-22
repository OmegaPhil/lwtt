
PREFIX = /usr/local
SRCDIR = .
BUILDLIBDIR = lib
BUILDDIR = build

JARFILE = lwtt.jar
MANIFEST = MANIFEST.MF

JAVAC = javac
INSTALL = install

CLASSPATH = $(BUILDLIBDIR)/swing-layout-1.0.3.jar

JAVACFLAGS = -classpath $(CLASSPATH)

PKGPATH = cz/aiken/util/lwtt
SRCPATH = $(SRCDIR)/$(PKGPATH)
PACKAGE = cz.aiken.util.lwtt


all: $(JARFILE)

$(JARFILE):
	mkdir -p $(BUILDDIR)
	javac $(JAVACFLAGS) -d $(BUILDDIR) $(SRCPATH)/*.java
	jar cfm $(JARFILE) $(MANIFEST) -C $(BUILDDIR) $(PKGPATH)/ 

clean:
	rm -f $(JARFILE)
	rm -rf $(BUILDDIR)

distclean: clean

.PHONY: all clean distclean

.POSIX:



