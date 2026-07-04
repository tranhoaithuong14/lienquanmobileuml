package com.lqm.combat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ViTriTest {

    @Test
    void khoangCachDenChinhNoLaKhong() {
        ViTri p = new ViTri(3, 4);

        assertEquals(0.0, p.khoangCachDen(p));
    }

    @Test
    void khoangCachDenDungCongThucEuclid() {
        ViTri a = new ViTri(0, 0);
        ViTri b = new ViTri(3, 4);

        assertEquals(5.0, a.khoangCachDen(b));
    }

    @Test
    void khoangCachDenDoiXung() {
        ViTri a = new ViTri(1, 2);
        ViTri b = new ViTri(7, 9);

        assertEquals(a.khoangCachDen(b), b.khoangCachDen(a));
    }

    @Test
    void khoangCachDenXuLyToaDoAm() {
        ViTri a = new ViTri(-3, -4);
        ViTri b = new ViTri(0, 0);

        assertEquals(5.0, a.khoangCachDen(b));
    }
}
