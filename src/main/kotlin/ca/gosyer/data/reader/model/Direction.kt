/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.data.reader.model

import kotlinx.serialization.Serializable

@Serializable
enum class Direction(val res: String) {
    Down("Down"),
    Left("RTL"),
    Right("LTR"),
    Up("Up")
}
