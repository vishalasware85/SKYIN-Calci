/*
 * Copyright (C) 2018 Duy Tran Le
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.skyncalci.calculator.symja.models;

/**
 * LimitItem
 * Created by Duy on 29-Dec-16.
 */

public class LimitItem extends IntegrateItem {
    /**
     * Limit item
     *
     * @param input - function
     * @param to    - upper limit, x -> inf
     */
    public LimitItem(String input, String to) {
        super(input, "", to);
    }

    @Override
    public String getInput() {
        return "Limit(" + input + "," + var + " -> " + to + ")";
    }
}
