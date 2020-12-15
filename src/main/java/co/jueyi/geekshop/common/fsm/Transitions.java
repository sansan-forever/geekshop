/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.common.fsm;

import java.util.HashMap;
import java.util.List;

/**
 * A type which is used to define valid states and transitions for a state machine based on {@link FSM}.
 *
 * Created on Dec, 2020 by @author bobo
 */
public class Transitions<T> extends HashMap<T, List<T>> {
}
