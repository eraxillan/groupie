package com.xwray.groupie;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.verify;

import androidx.annotation.NonNull;
import java.util.Objects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ItemTest {

    @Mock
    GroupAdapter<GroupieViewHolder> groupAdapter;

    @Test
    public void selfPositionIs0() {
        Item<?> item = new DummyItem();
        assertEquals(0, item.getPosition(item));
    }

    @Test
    public void positionIsNegative1IfItemIsNotSelf() {
        Item<?> item = new DummyItem();
        Item<?> differentItem = new DummyItem();
        assertEquals(-1, item.getPosition(differentItem));
    }

    @Test
    public void notifyChangeNotifiesParentObserver() {
        Item<?> item = new DummyItem();
        item.registerGroupDataObserver(groupAdapter);
        item.notifyChanged();

        verify(groupAdapter).onItemChanged(item, 0);
    }

    @Test
    public void isSameAsFailsIfViewTypeIsDifferent() {
        final DataItemTest itemA = new DataItemTest(0, 1, 0);
        final DataItemTest itemB = new DataItemTest(1, 1, 0);

        assertFalse(itemA.isSameAs(itemB));
    }

    @Test
    public void isSameAsSucceedsIfIdMatches() {
        final DataItemTest itemA = new DataItemTest(1, 1, 0);
        final DataItemTest itemB = new DataItemTest(1, 1, 0);
        final DataItemTest itemC = new DataItemTest(1, 2, 0);

        assertTrue(itemA.isSameAs(itemB));
        assertFalse(itemA.isSameAs(itemC));
    }

    @Test
    public void hasSameContentAsUsesEqualsByDefault() {
        final DataItemTest itemA = new DataItemTest(0, 1, 2);
        final DataItemTest itemB = new DataItemTest(0, 1, 2);
        final DataItemTest itemC = new DataItemTest(0, 1, 3);

        assertTrue(itemA.hasSameContentAs(itemB));
        assertFalse(itemA.hasSameContentAs(itemC));
    }

    private static class DataItemTest extends Item<GroupieViewHolder> {

        private final int layout;
        private final long id;
        private final int equalsComparison;

        DataItemTest(int layout, long id, int equalsComparison) {
            this.layout = layout;
            this.id = id;
            this.equalsComparison = equalsComparison;
        }

        @Override
        public void bind(@NonNull GroupieViewHolder viewHolder, int position) {

        }

        @Override
        public long getId() {
            return id;
        }

        @Override
        public int getLayout() {
            return layout;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DataItemTest dataItem = (DataItemTest) o;
            return equalsComparison == dataItem.equalsComparison;
        }

        @Override
        public int hashCode() {
            return Objects.hash(equalsComparison);
        }
    }
}