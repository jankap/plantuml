/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 *
 *
 */
package net.sourceforge.plantuml.classdiagram;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Objects;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.baraye.EntityUtils;
import net.sourceforge.plantuml.baraye.IGroup;
import net.sourceforge.plantuml.baraye.ILeaf;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.objectdiagram.AbstractClassOrObjectDiagram;
import net.sourceforge.plantuml.svek.image.EntityImageClass;

public class ClassDiagram extends AbstractClassOrObjectDiagram {

	public ClassDiagram(UmlSource source, Map<String, String> skinParam) {
		super(source, UmlDiagramType.CLASS, skinParam);
	}

	private Code getShortName1972(Code code) {
		final String separator = Objects.requireNonNull(getNamespaceSeparator());
		final String codeString = code.getName();
		final String namespace = getNamespace1972(code, getNamespaceSeparator());
		if (namespace == null) {
			return buildCode(codeString);
		}
		return buildCode(codeString.substring(namespace.length() + separator.length()));
	}

	@Override
	public ILeaf getOrCreateLeaf(Ident ident, Code code, LeafType type, USymbol symbol) {
		Objects.requireNonNull(ident);
		if (this.V1972()) {
			if (type == null) {
				type = LeafType.CLASS;
			}
			return getOrCreateLeafDefault(ident, code, type, symbol);
		}
		if (type == null) {
			code = code.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:");
			if (getNamespaceSeparator() == null) {
				return getOrCreateLeafDefault(ident, code, LeafType.CLASS, symbol);
			}
			code = getFullyQualifiedCode1972(code);
			if (super.leafExist(code)) {
				return getOrCreateLeafDefault(ident, code, LeafType.CLASS, symbol);
			}
			return createEntityWithNamespace1972(ident, code, Display.getWithNewlines(ident.getLast()), LeafType.CLASS,
					symbol);
		}
		if (getNamespaceSeparator() == null) {
			return getOrCreateLeafDefault(ident, code, type, symbol);
		}
		code = getFullyQualifiedCode1972(code);
		if (super.leafExist(code)) {
			return getOrCreateLeafDefault(ident, code, type, symbol);
		}
		return createEntityWithNamespace1972(ident, code, Display.getWithNewlines(ident.getLast()), type, symbol);
	}

	@Override
	public ILeaf createLeaf(Ident idNewLong, Code code, Display display, LeafType type, USymbol symbol) {
		Objects.requireNonNull(idNewLong);
		if (type != LeafType.ABSTRACT_CLASS && type != LeafType.ANNOTATION && type != LeafType.CLASS
				&& type != LeafType.INTERFACE && type != LeafType.ENUM && type != LeafType.LOLLIPOP_FULL
				&& type != LeafType.LOLLIPOP_HALF && type != LeafType.NOTE) {
			return super.createLeaf(idNewLong, code, display, type, symbol);
		}
		if (this.V1972()) {
			return super.createLeaf(idNewLong, code, display, type, symbol);
		}
		if (getNamespaceSeparator() == null) {
			return super.createLeaf(idNewLong, code, display, type, symbol);
		}
		code = getFullyQualifiedCode1972(code);
		if (super.leafExist(code)) {
			throw new IllegalArgumentException("Already known: " + code);
		}
		return createEntityWithNamespace1972(idNewLong, code, display, type, symbol);
	}

	private ILeaf createEntityWithNamespace1972(Ident id, Code fullyCode, Display display, LeafType type,
			USymbol symbol) {
		if (this.V1972())
			throw new UnsupportedOperationException();
		Objects.requireNonNull(id);
		final IGroup backupCurrentGroup = getCurrentGroup();
		final IGroup group = backupCurrentGroup;
		final String namespaceString = getNamespace1972(fullyCode, getNamespaceSeparator());
		if (namespaceString != null
				&& (EntityUtils.groupRoot(group) || group.getCodeGetName().equals(namespaceString) == false)) {
			final Code namespace = buildCode(namespaceString);
			final Display tmp = Display.getWithNewlines(namespaceString);
			final Ident newIdLong = buildLeafIdentSpecial(namespaceString);
			// final Ident newIdLong = buildLeafIdentSpecial2(namespaceString);
			gotoGroupExternal(newIdLong, namespace, tmp, namespace, GroupType.PACKAGE, getRootGroup());
		}
		final Display tmpDisplay;
		if (Display.isNull(display)) {
			tmpDisplay = Display.getWithNewlines(getShortName1972(fullyCode)).withCreoleMode(CreoleMode.SIMPLE_LINE);
		} else {
			tmpDisplay = display;
		}
		final ILeaf result = createLeafInternal(id, fullyCode, tmpDisplay, type, symbol);
		gotoThisGroup(backupCurrentGroup);
		return result;
	}

	@Override
	public final boolean leafExist(Code code) {
		if (getNamespaceSeparator() == null) {
			return super.leafExist(code);
		}
		return super.leafExist(getFullyQualifiedCode1972(code));
	}

	private boolean allowMixing;

	public void setAllowMixing(boolean allowMixing) {
		this.allowMixing = allowMixing;
	}

	public boolean isAllowMixing() {
		return allowMixing;
	}

	private int useLayoutExplicit = 0;

	public void layoutNewLine() {
		useLayoutExplicit++;
		incRawLayout();
	}

	@Override
	final protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		if (useLayoutExplicit != 0) {
			return exportLayoutExplicit(os, index, fileFormatOption);
		}
		return super.exportDiagramInternal(os, index, fileFormatOption);
	}

	final protected ImageData exportLayoutExplicit(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		final FullLayout fullLayout = new FullLayout();
		for (int i = 0; i <= useLayoutExplicit; i++) {
			final RowLayout rawLayout = getRawLayout(i);
			fullLayout.addRowLayout(rawLayout);
		}
		return createImageBuilder(fileFormatOption).annotations(false) // Backwards compatibility - this only applies
																		// when "layout_new_line" is used
				.drawable(fullLayout).write(os);
	}

	private RowLayout getRawLayout(int raw) {
		final RowLayout rawLayout = new RowLayout();
		for (ILeaf leaf : entityFactory.leafs()) {
			if (leaf.getRawLayout() == raw) {
				rawLayout.addLeaf(getEntityImageClass(leaf));
			}
		}
		return rawLayout;
	}

	private TextBlock getEntityImageClass(ILeaf entity) {
		return new EntityImageClass(entity, getSkinParam(), this);
	}

	@Override
	public String checkFinalError() {
		for (Link link : this.getLinks()) {
			final int len = link.getLength();
			if (len == 1) {
				for (Link link2 : this.getLinks()) {
					if (link2.sameConnections(link) && link2.getLength() != 1) {
						link2.setLength(1);
					}
				}
			}
		}
		this.applySingleStrategy();
		return super.checkFinalError();
	}

}
